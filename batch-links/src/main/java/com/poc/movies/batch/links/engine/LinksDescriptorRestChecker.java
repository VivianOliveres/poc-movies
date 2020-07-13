package com.poc.movies.batch.links.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.links.HttpUtils;
import com.poc.movies.batch.links.model.LinksDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;

@Slf4j
public class LinksDescriptorRestChecker implements ItemWriter<LinksDescriptor> {

    public static final String GET_URL = "/external/link";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String host;

    public LinksDescriptorRestChecker(String host) {
        Objects.requireNonNull(host);
        this.host = host;
    }

    @Override
    public void write(List<? extends LinksDescriptor> items) throws Exception {
        LinksDescriptor desc = items.get(0);

        // Check all links
        var body1 = HttpUtils.get(host + GET_URL + "/" + desc.getMovieId());
        LinksDescriptor value1 = MAPPER.readValue(body1, LinksDescriptor.class);
        if (!desc.equals(value1)) {
            throw new RuntimeException("Failed checking:\n\texpected: " + desc + "\n\tactual: " + value1);
        }

        // Check imdb
        String url2 = host + GET_URL + "/" + desc.getMovieId() + "/imdb";
        var body2 = HttpUtils.get(url2);
        var value2 = MAPPER.readValue(body2, LinksDescriptor.class);
        var expected2 = desc.withTmdbId(null);
        if (!expected2.equals(value2)) {
            throw new RuntimeException("Failed checking ImdbId:\n\tinitial: " + desc + "\n\texpected: " + expected2 + "\n\tactual: " + value2);
        }

        // Check tmdb
        String url3 = host + GET_URL + "/" + desc.getMovieId() + "/tmdb";
        var body3 = HttpUtils.get(url3);
        var value3 = MAPPER.readValue(body3, LinksDescriptor.class);
        var expected3 = desc.withImdbId(null);
        if (!expected3.equals(value3)) {
            throw new RuntimeException("Failed checking TmdbId:\n\tinitial: \" + desc + \"n\texpected: " + expected3 + "\n\tactual: " + value3);
        }
    }
}
