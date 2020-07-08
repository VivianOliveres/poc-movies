package com.poc.movies.inventory.requests;

import lombok.Value;

import javax.annotation.Nonnull;
import java.util.Set;

@Value
public class InsertMovieCommand {
    @Nonnull
    Long id;

    @Nonnull
    String title;

    @Nonnull
    Set<String> genres;
}
