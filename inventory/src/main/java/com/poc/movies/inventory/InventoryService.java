package com.poc.movies.inventory;

import com.google.common.collect.Lists;
import com.poc.movies.inventory.db.*;
import com.poc.movies.inventory.model.Movie;
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
        Map<String, CategoryEntity> categoriesByName = doInsertCategories(movies);

        // Movies
        doInsertMovies(movies);

        // MovieCategory
        doInsertMovieCategories(movies, categoriesByName);
    }

    private Map<String, CategoryEntity> doInsertCategories(List<Movie> movies) {
        Set<String> categoryNames = movies.stream().flatMap(movie -> movie.getCategories().stream()).collect(toSet());
        return getAndInsertCategories(categoryNames);
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

    private void doInsertMovies(List<Movie> movies) {
        movies.forEach(movie -> {
            try {
                movieRepo.insertOne(movie.getId(), movie.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void doInsertMovieCategories(List<Movie> movies, Map<String, CategoryEntity> categoriesByName) {
        Set<MovieCategoryEntity> movieCategoryToInsert = movies.stream()
                .flatMap(movie -> movie.getCategories().stream().map(categoryName -> new MovieCategoryEntity(null, movie.getId(), categoriesByName.get(categoryName).getCategoryId())))
                .collect(toSet());
        movieCategoryRepo.saveAll(movieCategoryToInsert);
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

    public void update(Movie movie) {
        // Categories
        Map<String, CategoryEntity> categoriesByName = doInsertCategories(List.of(movie));

        // Movies
        MovieEntity movieEntity = new MovieEntity(movie.getId(), movie.getTitle());
        movieRepo.save(movieEntity);

        // MovieCategory
        doUpdateMovieCategories(movie, categoriesByName);
    }

    private void doUpdateMovieCategories(Movie movie, Map<String, CategoryEntity> categoriesByName) {
        movieCategoryRepo.deleteByMovieId(movie.getId());

        var movieCategoryToInsert = movie.getCategories().stream()
                .map(categoryName -> new MovieCategoryEntity(null, movie.getId(), categoriesByName.get(categoryName).getCategoryId()))
                .collect(toSet());

        movieCategoryRepo.saveAll(movieCategoryToInsert);
    }
}
