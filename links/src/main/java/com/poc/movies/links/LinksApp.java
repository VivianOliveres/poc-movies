package com.poc.movies.links;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@EnableJdbcRepositories
@SpringBootApplication
public class LinksApp {

    public static void main(String[] args) {
        SpringApplication.run(LinksApp.class, args);
    }
}
