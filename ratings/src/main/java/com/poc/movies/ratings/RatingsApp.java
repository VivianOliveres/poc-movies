package com.poc.movies.ratings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@EnableJdbcRepositories
@SpringBootApplication
public class RatingsApp {

    public static void main(String[] args) {
        SpringApplication.run(RatingsApp.class, args);
    }

}
