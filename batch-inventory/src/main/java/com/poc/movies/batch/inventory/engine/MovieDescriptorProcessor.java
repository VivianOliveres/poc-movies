package com.poc.movies.batch.inventory.engine;

import com.poc.movies.batch.inventory.model.MovieDescriptor;
import org.springframework.batch.item.ItemProcessor;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class MovieDescriptorProcessor implements ItemProcessor<MovieDescriptor, MovieDescriptor> {

    @Override
    public MovieDescriptor process(MovieDescriptor item) {
        Set<String> categories = item.getCategories().stream()
                .map(String::toLowerCase)
                .filter(str -> !str.equals("(no genres listed)"))
                .collect(toSet());
        return item.withCategories(categories);
    }
}
