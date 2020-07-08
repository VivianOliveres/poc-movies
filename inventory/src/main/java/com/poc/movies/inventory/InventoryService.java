package com.poc.movies.inventory;

import com.google.common.collect.Lists;
import com.poc.movies.inventory.db.*;
import com.poc.movies.inventory.exceptions.InsertException;
import com.poc.movies.inventory.model.Movie;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

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

    public Movie insert(Movie movie) throws InsertException {
        try {
            return doInsert(movie);

        } catch (DataAccessException e) {
            throw new InsertException("Can not insert " + movie, e);
        }
    }

    private Movie doInsert(Movie movie) throws DataAccessException {
        Map<String, CategoryEntity> categories = categoryRepo.findAllByCategoryName(movie.getCategories())
                .stream()
                .collect(toMap(CategoryEntity::getCategoryName, identity()));

        // Categories
        Set<CategoryEntity> categoriesToInsert = movie.getCategories().stream()
                .filter(categoryName -> !categories.containsKey(categoryName))
                .map(categoryName -> new CategoryEntity(null, categoryName))
                .collect(Collectors.toSet());
        List<CategoryEntity> categoriesInserted = Lists.newArrayList(categoryRepo.saveAll(categoriesToInsert));

        // Movie
        movieRepo.insertOne(movie.getId(), movie.getTitle());

        // MovieCategory
        Set<MovieCategoryEntity> movieCategoryToInsert = Stream.concat(categories.values().stream(), categoriesInserted.stream())
                .map(categoryEntity -> new MovieCategoryEntity(null, movie.getId(), categoryEntity.getCategoryId()))
                .collect(Collectors.toSet());
        movieCategoryRepo.saveAll(movieCategoryToInsert);

        return movie;
    }

    public Movie getById(long id) {
        return movieRepo.findById(id)
                .map(this::mapToModel)
                .orElseThrow(() -> new IllegalArgumentException("Movie [" + id + "] does not exist"));
    }

    private Movie mapToModel(MovieEntity movieEntity) {
        Set<String> categories = categoryRepo.findAllByMovieId(movieEntity.getMovieId()).stream()
                .map(CategoryEntity::getCategoryName)
                .collect(Collectors.toSet());
        return new Movie(movieEntity.getMovieId(), movieEntity.getTitle(), categories);
    }
}
