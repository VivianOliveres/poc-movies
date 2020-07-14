package com.poc.movies.batch.inventory.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.inventory.model.MovieDescriptor;
import com.poc.movies.batch.links.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MovieDescriptorRestWriter implements ItemWriter<MovieDescriptor> {

    public static final String POST_URL = "/inventory/movies";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String host;

    public MovieDescriptorRestWriter(String host) {
        Objects.requireNonNull(host);
        this.host = host;
    }

    @Override
    public void write(List<? extends MovieDescriptor> items) throws Exception {
        var json = toJson(items).get();
        HttpUtils.post(host + POST_URL, json);
    }

    private Optional<String> toJson(List<? extends MovieDescriptor> descs) {
        try {
            return Optional.of(MAPPER.writeValueAsString(descs));

        } catch (JsonProcessingException e) {
            log.error("Fail to convert into json: " + descs, e);
            return Optional.empty();
        }
    }

}
