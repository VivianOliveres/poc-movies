package com.poc.movies.tags.db;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<UserTagEntity, Long> {

    @Query("SELECT * FROM User_Tag WHERE movie_id = :movieId")
    List<UserTagEntity> findAllByMovieId(long movieId);

    @Query("SELECT * FROM User_Tag WHERE movie_id = :movieId AND user_id = :userId")
    List<UserTagEntity> findAllByMovieIdAndUserId(long movieId, long userId);

    @Query("SELECT * FROM User_Tag WHERE user_id = :userId")
    List<UserTagEntity> findAllByUserId(long userId);
}
