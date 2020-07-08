package com.poc.movies.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.annotation.Nonnull;
import java.util.Set;

@Value
@AllArgsConstructor
public class Movie {

    @Nonnull
    Long id;

    @Nonnull
    String title;

    @Nonnull
    Set<String> categories;
}
