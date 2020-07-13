package com.poc.movies.batch.links.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.links.HttpUtils;
import com.poc.movies.batch.links.model.LinksDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class LinksDescriptorRestWriter implements ItemWriter<LinksDescriptor> {

    public static final String POST_URL = "/external/links";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String host;

    public LinksDescriptorRestWriter(String host) {
        Objects.requireNonNull(host);
        this.host = host;
    }

    @Override
    public void write(List<? extends LinksDescriptor> items) throws Exception {
        var json = toJson(items).get();
        HttpUtils.post(host + POST_URL, json);
    }

    private Optional<String> toJson(List<? extends LinksDescriptor> descs) {
        try {
            return Optional.of(MAPPER.writeValueAsString(descs));

        } catch (JsonProcessingException e) {
            log.error("Fail to convert into json: " + descs, e);
            return Optional.empty();
        }
    }

}
