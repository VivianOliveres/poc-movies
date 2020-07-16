package com.poc.movies.ratings.batch.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.HttpUtils;
import com.poc.movies.ratings.batch.model.RatingsDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;

@Slf4j
public class RatingsDescriptorRestChecker implements ItemWriter<RatingsDescriptor> {

    public static final String GET_URL = "/ratings";

    private final ObjectMapper mapper;

    private final String host;

    public RatingsDescriptorRestChecker(String host, ObjectMapper mapper) {
        Objects.requireNonNull(host);
        Objects.requireNonNull(mapper);
        this.host = host;
        this.mapper = mapper;
    }

    @Override
    public void write(List<? extends RatingsDescriptor> items) throws Exception {
        RatingsDescriptor desc = items.get(0);

        // Check all links
        var body1 = HttpUtils.get(getUrl(desc));
        RatingsDescriptor value1 = mapper.readValue(body1, RatingsDescriptor.class);
        if (!desc.equals(value1)) {
            throw new RuntimeException("Failed checking:\n\texpected: " + desc + "\n\tactual: " + value1);
        }
    }

    private String getUrl(RatingsDescriptor desc) {
        return host + GET_URL + "/movie/" + desc.getMovieId() + "/user/" + desc.getUserId();
    }
}
