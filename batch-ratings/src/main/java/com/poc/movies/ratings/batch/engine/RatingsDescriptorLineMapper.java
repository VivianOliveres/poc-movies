package com.poc.movies.ratings.batch.engine;

import com.poc.movies.ratings.batch.model.RatingsDescriptor;
import org.springframework.batch.item.file.LineMapper;

public class RatingsDescriptorLineMapper implements LineMapper<RatingsDescriptor> {

    @Override
    public RatingsDescriptor mapLine(String line, int lineNumber) throws NumberFormatException {
        //userId,movieId,rating,timestamp
        String[] split = line.split(",");

        long userId = Long.parseLong(split[0]);
        long movieId = Long.parseLong(split[1]);
        double rating = Double.parseDouble(split[2]);
        long timestamp = Long.parseLong(split[3]);

        return new RatingsDescriptor(movieId, userId, rating, timestamp);
    }
}
