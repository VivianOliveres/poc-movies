package com.poc.movies.batch.links.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinksDescriptor {

    long movieId;

    @With
    String imdbId;

    @With
    Long tmdbId;
}
