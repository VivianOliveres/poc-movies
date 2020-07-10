package com.poc.movies.inventory.db;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class MovieFacadeRepository {

    private MovieRepository movieRepository;
    private MovieCategoryRepository movieCategoryRepository;

    public MovieFacadeRepository(MovieRepository movieRepository, MovieCategoryRepository movieCategoryRepository) {
        this.movieRepository = movieRepository;
        this.movieCategoryRepository = movieCategoryRepository;
    }

    @Transactional
    public MovieEntity insert(long movieId, String title, List<Long> categoryIds) {
        movieRepository.insertOne(movieId, title);

        List<MovieCategoryEntity> movieCategoryEntities = categoryIds.stream()
                .map(categoryId -> new MovieCategoryEntity(null, movieId, categoryId))
                .collect(toList());
        movieCategoryRepository.saveAll(movieCategoryEntities);

        return new MovieEntity(movieId, title);
    }

    @Transactional
    public MovieEntity update(long movieId, String title, List<Long> categoryIds) {
        // Update movie
        MovieEntity entity = movieRepository.save(new MovieEntity(movieId, title));

        // Clear old category references
        movieCategoryRepository.deleteByMovieId(movieId);

        // Insert new category references
        List<MovieCategoryEntity> movieCategoryEntities = categoryIds.stream()
                .map(categoryId -> new MovieCategoryEntity(null, movieId, categoryId))
                .collect(toList());
        movieCategoryRepository.saveAll(movieCategoryEntities);

        return entity;
    }

    public Optional<MovieEntity> findById(long id) {
        return movieRepository.findById(id);
    }

    public Iterable<MovieEntity> findAllById(List<Long> ids) {
        return movieRepository.findAllById(ids);
    }
}
