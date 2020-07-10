package com.poc.movies.batch.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDescriptor {

    long id;

    String title;

    @With
    Set<String> categories;

}
