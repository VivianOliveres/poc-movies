package com.poc.movies.batch.tags.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.HttpUtils;
import com.poc.movies.batch.tags.model.TagsDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class TagRestWriter implements ItemWriter<TagsDescriptor> {

    public static final String POST_URL = "/tags/bulk";

    private final String host;
    private final ObjectMapper mapper;

    public TagRestWriter(String host, ObjectMapper mapper) {
        Objects.requireNonNull(host);
        Objects.requireNonNull(mapper);
        this.host = host;
        this.mapper = mapper;
    }

    @Override
    public void write(List<? extends TagsDescriptor> items) throws Exception {
        var json = toJson(items).get();
        HttpUtils.post(host + POST_URL, json);
    }

    private Optional<String> toJson(List<? extends TagsDescriptor> descs) {
        try {
            return Optional.of(mapper.writeValueAsString(descs));

        } catch (JsonProcessingException e) {
            log.error("Fail to convert into json: " + descs, e);
            return Optional.empty();
        }
    }

}
