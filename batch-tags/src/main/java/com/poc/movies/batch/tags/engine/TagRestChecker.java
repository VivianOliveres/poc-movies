package com.poc.movies.batch.tags.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.HttpUtils;
import com.poc.movies.batch.tags.model.TagsDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Objects;

@Slf4j
public class TagRestChecker implements ItemWriter<TagsDescriptor> {

    public static final String GET_URL = "/tags";

    private final ObjectMapper mapper;

    private final String host;

    public TagRestChecker(String host, ObjectMapper mapper) {
        Objects.requireNonNull(host);
        Objects.requireNonNull(mapper);
        this.host = host;
        this.mapper = mapper;
    }

    @Override
    public void write(List<? extends TagsDescriptor> items) throws Exception {
        TagsDescriptor desc = items.get(0);

        // Check
        var body1 = HttpUtils.get(getUrl(desc));
        List<TagsDescriptor> value1 = mapper.readerForListOf(TagsDescriptor.class).readValue(body1);
        if (!value1.contains(desc)) {
            throw new RuntimeException("Failed checking:\n\texpected: " + desc + "\n\tactual: " + value1);
        }
    }

    private String getUrl(TagsDescriptor desc) {
        return host + GET_URL + "/movie/" + desc.getMovieId() + "/user/" + desc.getUserId();
    }
}
