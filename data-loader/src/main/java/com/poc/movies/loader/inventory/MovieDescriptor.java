package com.poc.movies.loader.inventory;

import lombok.Value;

import java.util.Set;

@Value
public class MovieDescriptor {
    long id;
    String title;
    Set<String> categories;
}
