package com.poc.movies.ratings.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.poc.movies.ratings.batch.model.RatingsDescriptor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LolTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void test() throws JsonProcessingException {
        var mapper = new ObjectMapper().registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        var toto = new RatingsDescriptor(1, 2, 3.5, 1112486027L);
        String str = mapper.writeValueAsString(toto);
        System.out.println(str);
        RatingsDescriptor tutu = mapper.readValue(str, RatingsDescriptor.class);
        System.out.println(tutu);
    }
}
