package com.poc.movies.inventory.db;

import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("Movies_Categories")
public class MovieCategoryEntity {

    @Id
    @With
    @Column("movies_categories_id")
    Long movieCategoryId;

    @With
    @Column("movie_id")
    long movieId;

    @With
    @Column("category_id")
    long categoryId;
}
