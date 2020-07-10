package com.poc.movies.batch.inventory.engine;

import com.poc.movies.batch.inventory.model.MovieDescriptor;
import org.springframework.batch.item.file.LineMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MovieDescriptorLineMapper implements LineMapper<MovieDescriptor> {

    /**
     * Extract a movie from a string with format: $movieId,$title,genre1|genre2|...|genreN. <br>
     * Note that $title can contains zero or many coma.
     */
    @Override
    public MovieDescriptor mapLine(String line, int lineNumber) throws NumberFormatException {
        String[] split = line.split(",");

        // Extract id
        long id = Long.parseLong(split[0]);

        // Extract title (title field could contains ',')
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
}
