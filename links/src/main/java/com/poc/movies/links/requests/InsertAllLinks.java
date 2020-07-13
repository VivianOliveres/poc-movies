package com.poc.movies.links.requests;

import lombok.Value;

@Value
public class InsertAllLinks {

    long movieId;

    String imdbId;

    Long tmdbId;
}
