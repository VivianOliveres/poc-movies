package com.poc.movies.batch.links.engine;

import com.poc.movies.batch.links.model.LinksDescriptor;
import org.springframework.batch.item.file.LineMapper;

public class LinksDescriptorLineMapper implements LineMapper<LinksDescriptor> {

    @Override
    public LinksDescriptor mapLine(String line, int lineNumber) throws NumberFormatException {
        String[] split = line.split(",");

        long movieId = Long.parseLong(split[0]);

        String imdbId = split[1];

        Long tmdbId = split.length < 3 || split[2] == null ? null : Long.parseLong(split[2]);

        // Data invalid for this movie:
        //690,0111613,105045
        //1533,0117398,105045
        if (movieId == 1533) {
            return new LinksDescriptor(movieId, imdbId, 24183L);
        }

        // Data invalid for this movie:
        //7587,0062229,5511
        //27136,0165303,5511
        if (movieId == 27136) {
            return new LinksDescriptor(movieId, imdbId, 45758L);
        }

        // Data invalid for this movie:
        //8795,0275083,23305
        //27528,0295682,23305
        if (movieId == 8795) {
            return new LinksDescriptor(movieId, imdbId, 1436L);
        }
        if (movieId == 27528) {
            return new LinksDescriptor(movieId, imdbId, 23305L);
        }

        // Data invalid for this movie 31026 and 123107
        if (movieId == 31026 || movieId == 123107) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 66140 and 85070
        if (movieId == 66140 || movieId == 85070) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 47237
        if (movieId == 47237) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 3799 and 91752
        if (movieId == 3799 || movieId == 91752) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 80330
        if (movieId == 80330) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 55132
        if (movieId == 55132) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 70507
        if (movieId == 70507) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 2679
        if (movieId == 2679) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 98572
        if (movieId == 98572) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 77141
        if (movieId == 77141) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 87279
        if (movieId == 87279) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 102367
        if (movieId == 102367) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 4484
        if (movieId == 4484) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        // Data invalid for this movie 98460
        if (movieId == 98460) {
            return new LinksDescriptor(movieId, imdbId, null);
        }

        return new LinksDescriptor(movieId, imdbId, tmdbId);
    }
}
