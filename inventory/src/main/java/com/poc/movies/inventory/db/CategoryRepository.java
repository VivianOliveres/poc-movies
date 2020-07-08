package com.poc.movies.inventory.db;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

    @Query("SELECT c.category_id, c.category_name FROM Categories AS c " +
            "LEFT JOIN Movies_Categories AS mc ON c.category_id = mc.category_id " +
            "WHERE mc.movie_id = :movieId")
    Set<CategoryEntity> findAllByMovieId(long movieId);

    @Query("SELECT category_id, category_name FROM Categories WHERE category_name IN (:categoryName)")
    Set<CategoryEntity> findAllByCategoryName(Set<String> categoryName);

}
