package com.poc.movies.inventory.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<MovieEntity, Long> {

    /**
     * Insert a new {@link MovieEntity}.
     * This method should be preferred instead of {@link CrudRepository#save} in order to force an id.
     */
    @Modifying
    @Query("INSERT INTO Movies VALUES (:id, :title)")
    long insertOne(long id, String title);
}
