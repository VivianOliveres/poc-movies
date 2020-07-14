package com.poc.movies.batch.inventory.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.inventory.model.MovieDescriptor;
import com.poc.movies.batch.links.HttpUtils;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;

public class MovieDescriptorRestChecker implements ItemWriter<MovieDescriptor> {

    public static final String GET_URL = "/inventory/movie";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String host;

    public MovieDescriptorRestChecker(String host) {
        Objects.requireNonNull(host);
        this.host = host;
    }

    @Override
    public void write(List<? extends MovieDescriptor> items) throws Exception {
        MovieDescriptor desc = items.get(0);
        String body = HttpUtils.get(host + GET_URL + "/" + desc.getId());
        MovieDescriptor returnedMovie = MAPPER.readValue(body, MovieDescriptor.class);
        if (!desc.equals(returnedMovie)) {
            throw new RuntimeException("Failed checking from/to:\n\t" + desc + "\n\t" + returnedMovie);
        }

    }
}
