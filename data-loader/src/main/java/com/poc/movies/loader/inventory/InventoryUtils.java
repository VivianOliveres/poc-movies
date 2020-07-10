package com.poc.movies.loader.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class InventoryUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String GET_URL = "/inventory/movie";
    public static final String POST_URL = "/inventory/movies";

    /**
     * Extract a movie from a string with format: $movieId,$title,genre1|genre2|...|genreN. <br>
     * Note that $title can contains zero or many coma.
     */
    public static MovieDescriptor extractMovie(String csvLine) {
        String[] split = csvLine.split(",");
        long id = Long.parseLong(split[0]);

        // Extract title
        List<String> titleCells = Arrays.asList(split).subList(1, split.length - 1);
        String title = String.join(",", titleCells);
        if (title.startsWith("\"")) {
            title = title.substring(1, title.length() - 1);
        }

        // Extract genres
        String genresAsStr = split[split.length - 1];
        String[] splitedGenres = genresAsStr.split("\\|");
        Set<String> genres = Set.of(splitedGenres);

        return new MovieDescriptor(id, title, genres);
    }

    public static Optional<String> toJson(List<? extends MovieDescriptor> descs) {
        try {
            return Optional.of(MAPPER.writeValueAsString(descs));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
