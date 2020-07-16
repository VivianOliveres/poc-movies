package com.poc.movies.batch.inventory.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.HttpUtils;
import com.poc.movies.batch.inventory.model.MovieDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MovieDescriptorRestWriter implements ItemWriter<MovieDescriptor> {

    public static final String POST_URL = "/inventory/movies";

    private final String host;
    private static ObjectMapper mapper;

    public MovieDescriptorRestWriter(String host, ObjectMapper mapper) {
        Objects.requireNonNull(host);
        this.host = host;
        this.mapper = mapper;
    }

    @Override
    public void write(List<? extends MovieDescriptor> items) throws Exception {
        var json = toJson(items).get();
        HttpUtils.post(host + POST_URL, json);
    }

    private Optional<String> toJson(List<? extends MovieDescriptor> descs) {
        try {
            return Optional.of(mapper.writeValueAsString(descs));

        } catch (JsonProcessingException e) {
            log.error("Fail to convert into json: " + descs, e);
            return Optional.empty();
        }
    }

}
