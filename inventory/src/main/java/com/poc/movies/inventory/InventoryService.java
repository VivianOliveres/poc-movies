package com.poc.movies.inventory;

import com.google.common.collect.Lists;
import com.poc.movies.inventory.db.CategoryEntity;
import com.poc.movies.inventory.db.CategoryRepository;
import com.poc.movies.inventory.db.MovieEntity;
import com.poc.movies.inventory.db.MovieFacadeRepository;
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

    private MovieFacadeRepository movieFacadeRepository;
    private CategoryRepository categoryRepo;

    public InventoryService(CategoryRepository categoryRepo, MovieFacadeRepository movieFacadeRepository) {
        this.categoryRepo = categoryRepo;
        this.movieFacadeRepository = movieFacadeRepository;
    }

    public void insert(Movie movie) {
        insert(List.of(movie));
    }

    public void insert(List<Movie> movies) {
        // Categories
        Map<String, CategoryEntity> categoriesByName = doInsertCategories(movies);

        // Movie and MovieCategory
        doInsertMovies(movies, categoriesByName);
    }

    private Map<String, CategoryEntity> doInsertCategories(List<Movie> movies) {
        Set<String> categoryNames = movies.stream().flatMap(movie -> movie.getCategories().stream()).collect(toSet());

        Map<String, CategoryEntity> categories = categoryRepo.findAllByCategoryName(categoryNames)
                .stream()
                .collect(toMap(CategoryEntity::getCategoryName, identity()));

        // Insert categories that does not already exists
        List<CategoryEntity> categoryEntitiesToInsert = categoryNames.stream()
                .filter(categoryName -> !categories.containsKey(categoryName))
                .map(categoryName -> new CategoryEntity(null, categoryName))
                .collect(toList());
        if (categoryEntitiesToInsert.isEmpty()) {
            // No category needs to be created
            return categories;
        }

        // Insert categories missing
        categoryEntitiesToInsert
                .forEach(categoryEntity -> categoryRepo.insertIgnoreOne(categoryEntity.getCategoryName()));

        return categoryRepo.findAllByCategoryName(categoryNames)
                .stream()
                .collect(toMap(CategoryEntity::getCategoryName, identity()));
    }

    private void doInsertMovies(List<Movie> movies, Map<String, CategoryEntity> categoriesByName) {
        movies.forEach(movie -> {
            try {
                List<Long> categoryIds = movie.getCategories().stream()
                        .map(categoriesByName::get)
                        .map(CategoryEntity::getCategoryId)
                        .collect(toList());
                movieFacadeRepository.insert(movie.getId(), movie.getTitle(), categoryIds);

            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public Movie getByMovieId(long id) {
        return movieFacadeRepository.findById(id)
                .map(this::mapToModel)
                .orElseThrow(() -> new IllegalArgumentException("Movie [" + id + "] does not exist"));
    }

    public List<Movie> getByMovieIds(List<Long> ids) {
        return Lists.newArrayList(movieFacadeRepository.findAllById(ids))
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
        List<Long> categoryIds = movie.getCategories().stream()
                .map(categoriesByName::get)
                .map(CategoryEntity::getCategoryId)
                .collect(toList());
        movieFacadeRepository.update(movie.getId(), movie.getTitle(), categoryIds);
    }
}
