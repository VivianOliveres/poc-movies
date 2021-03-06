package com.poc.movies.batch.links.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.HttpUtils;
import com.poc.movies.batch.links.model.LinksDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class LinksDescriptorRestWriter implements ItemWriter<LinksDescriptor> {

    public static final String POST_URL = "/external/links";

    private final ObjectMapper mapper;

    private final String host;

    public LinksDescriptorRestWriter(String host, ObjectMapper mapper) {
        Objects.requireNonNull(host);
        Objects.requireNonNull(mapper);
        this.host = host;
        this.mapper = mapper;
    }

    @Override
    public void write(List<? extends LinksDescriptor> items) throws Exception {
        var json = toJson(items).get();
        HttpUtils.post(host + POST_URL, json);
    }

    private Optional<String> toJson(List<? extends LinksDescriptor> descs) {
        try {
            return Optional.of(mapper.writeValueAsString(descs));

        } catch (JsonProcessingException e) {
            log.error("Fail to convert into json: " + descs, e);
            return Optional.empty();
        }
    }

}
