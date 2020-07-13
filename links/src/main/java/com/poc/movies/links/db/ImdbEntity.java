package com.poc.movies.links.db;

import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("Imdb")
public class ImdbEntity {

    @Id
    @Column("movie_id")
    long movieId;

    @With
    @Column("imdb_id")
    String imdbId;
}
