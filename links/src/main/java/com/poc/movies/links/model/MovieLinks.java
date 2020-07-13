package com.poc.movies.links.model;

import lombok.Value;
import lombok.With;

import javax.annotation.Nonnull;
import java.util.Optional;

@Value
public class MovieLinks {

    long movieId;

    @With
    String imdbId;

    @With
    Long tmdbId;


    public MovieLinks(long movieLensId) {
        this.movieId = movieLensId;
        imdbId = null;
        tmdbId = null;
    }

    public MovieLinks(long movieLensId, @Nonnull Optional<String> maybeImdb, @Nonnull Optional<Long> maybeTmdb) {
        this.movieId = movieLensId;
        imdbId = maybeImdb.orElse(null);
        tmdbId = maybeTmdb.orElse(null);
    }

    public MovieLinks(long movieLensId, String imdb, Long tmdb) {
        this(movieLensId, Optional.ofNullable(imdb), Optional.ofNullable(tmdb));
    }

}
