package com.poc.movies.loader.inventory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class MovieDescriptor {

    long id;

    String title;

    @With
    Set<String> categories;

    public MovieDescriptor(long id, String title, Set<String> categories) {
        this.id = id;
        this.title = title;
        this.categories = categories.stream().map(String::toLowerCase).collect(toSet());
    }
}
