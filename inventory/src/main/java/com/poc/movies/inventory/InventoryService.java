package com.poc.movies.inventory;

import com.google.common.collect.Lists;
import com.poc.movies.inventory.db.*;
import com.poc.movies.inventory.model.Movie;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
public class InventoryService {

    private MovieRepository movieRepo;
    private CategoryRepository categoryRepo;
    private MovieCategoryRepository movieCategoryRepo;

    public InventoryService(MovieRepository movieRepo, CategoryRepository categoryRepo, MovieCategoryRepository movieCategoryRepo) {
        this.movieRepo = movieRepo;
        this.categoryRepo = categoryRepo;
        this.movieCategoryRepo = movieCategoryRepo;
    }

    public void insert(Movie movie) {
        insert(List.of(movie));
    }

    public void insert(List<Movie> movies) {
        // Categories
        Set<String> categoryNames = movies.stream().flatMap(movie -> movie.getCategories().stream()).collect(toSet());
        Map<String, CategoryEntity> categoriesByName = getAndInsertCategories(categoryNames);

        // Movies
        movies.forEach(movie -> {
            try {
                movieRepo.insertOne(movie.getId(), movie.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // MovieCategory
        Set<MovieCategoryEntity> movieCategoryToInsert = movies.stream()
                .flatMap(movie -> movie.getCategories().stream().map(categoryName -> new MovieCategoryEntity(null, movie.getId(), categoriesByName.get(categoryName).getCategoryId())))
                .collect(toSet());
        movieCategoryRepo.saveAll(movieCategoryToInsert);
    }

    private Movie doInsert(Movie movie) throws DataAccessException {
        // Categories
        Map<String, CategoryEntity> categories = getAndInsertCategories(movie.getCategories());

        // Movie
        movieRepo.insertOne(movie.getId(), movie.getTitle());

        // MovieCategory
        Set<MovieCategoryEntity> movieCategoryToInsert = categories.values().stream()
                .map(categoryEntity -> new MovieCategoryEntity(null, movie.getId(), categoryEntity.getCategoryId()))
                .collect(toSet());
        movieCategoryRepo.saveAll(movieCategoryToInsert);

        return movie;
    }

    private Map<String, CategoryEntity> getAndInsertCategories(Set<String> categoryNames) {
        Map<String, CategoryEntity> categories = categoryRepo.findAllByCategoryName(categoryNames)
                .stream()
                .collect(toMap(CategoryEntity::getCategoryName, identity()));

        // Categories
        categoryNames.stream()
                .filter(categoryName -> !categories.containsKey(categoryName))
                .map(categoryName -> new CategoryEntity(null, categoryName))
                .forEach(categoryEntity -> categoryRepo.insertIgnoreOne(categoryEntity.getCategoryName()));

        return categoryRepo.findAllByCategoryName(categoryNames)
                .stream()
                .collect(toMap(CategoryEntity::getCategoryName, identity()));
    }

    public Movie getByMovieId(long id) {
        return movieRepo.findById(id)
                .map(this::mapToModel)
                .orElseThrow(() -> new IllegalArgumentException("Movie [" + id + "] does not exist"));
    }

    public List<Movie> getByMovieIds(List<Long> ids) {
        return Lists.newArrayList(movieRepo.findAllById(ids))
                .stream()
                .map(this::mapToModel)
                .collect(toList());
    }

    private Movie mapToModel(MovieEntity movieEntity) {
        Set<String> categories = categoryRepo.findAllByMovieId(movieEntity.getMovieId()).stream()
                .map(CategoryEntity::getCategoryName)
                .collect(toSet());
        return new Movie(movieEntity.getMovieId(), movieEntity.getTitle(), categories);
    }
}
