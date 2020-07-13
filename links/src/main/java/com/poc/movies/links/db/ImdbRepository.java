package com.poc.movies.links.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface ImdbRepository extends CrudRepository<ImdbEntity, Long> {

    @Modifying
    @Query("INSERT IGNORE INTO Imdb (movie_id, imdb_id) VALUES (:movieId, :imdbId)")
    int insertIgnoreOne(long movieId, String imdbId);

    @Modifying
    @Query("UPDATE Imdb SET imdb_id = :imdbId WHERE movie_id = :movieId")
    int updateOne(long movieId, String imdbId);

    @Modifying
    @Query("DELETE IGNORE From Imdb WHERE movie_id = :movieId")
    void deleteIgnoreOne(long movieId);
}
