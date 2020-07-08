package com.poc.movies.inventory.model;

import lombok.Value;

import javax.annotation.Nonnull;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Value
public class Movie {

    @Nonnull
    Long id;

    @Nonnull
    String title;

    @Nonnull
    Set<String> categories;

    public Movie(@Nonnull Long id, @Nonnull String title, @Nonnull Set<String> categories) {
        this.id = id;
        this.title = title;
        this.categories = categories.stream().map(String::toLowerCase).map(String::trim).collect(toSet());
    }
}
