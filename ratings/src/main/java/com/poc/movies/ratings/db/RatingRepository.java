package com.poc.movies.ratings.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends CrudRepository<RatingEntity, Long> {

    @Query("SELECT * FROM Ratings WHERE movie_id = :movieId AND user_id = :userId")
    Optional<RatingEntity> findByIds(long movieId, long userId);

    @Query("SELECT * FROM Ratings WHERE user_id = :userId")
    List<RatingEntity> findByUserId(long userId);

    @Query("SELECT * FROM Ratings WHERE movie_id = :movieId")
    List<RatingEntity> findByMovieId(long movieId);
}
