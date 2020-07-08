package com.poc.movies.inventory.db;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("Movies")
public class MovieEntity {

    @Id
    @Column("movie_id")
    long movieId;

    @Column("title")
    String title;
}
