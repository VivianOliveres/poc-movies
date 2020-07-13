package com.poc.movies.links.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface TmdbRepository extends CrudRepository<TmdbEntity, Long> {

    @Modifying
    @Query("INSERT IGNORE INTO Tmdb (movie_id, tmdb_id) VALUES (:movieId, :tmdbId)")
    int insertIgnoreOne(long movieId, long tmdbId);

    @Modifying
    @Query("UPDATE Tmdb SET tmdb_id = :tmdbId WHERE movie_id = :movieId")
    int updateOne(long movieId, Long tmdbId);

    @Modifying
    @Query("DELETE IGNORE From Tmdb WHERE movie_id = :movieId")
    void deleteIgnoreOne(long movieId);
}
