package com.poc.movies.inventory.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface MovieCategoryRepository extends CrudRepository<MovieCategoryEntity, Long> {

    @Modifying
    @Query("DELETE FROM Movies_Categories WHERE movie_id = :movieId")
    long deleteByMovieId(long movieId);
}
