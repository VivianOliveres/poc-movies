package com.poc.movies.links.db;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("Tmdb")
public class TmdbEntity {

    @Id
    @Column("movie_id")
    long movieId;

    @Column("tmdb_id")
    long tmdbId;
}
