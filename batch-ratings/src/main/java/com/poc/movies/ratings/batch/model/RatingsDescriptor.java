package com.poc.movies.ratings.batch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.util.TimeZone.getDefault;

@Data
@NoArgsConstructor
public class RatingsDescriptor {

    long movieId;

    long userId;

    double ratingValue;

    LocalDateTime ratingTime;

    public RatingsDescriptor(long movieId, long userId, double ratingValue, LocalDateTime ratingTime) {
        this.movieId = movieId;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.ratingTime = ratingTime;
    }

    public RatingsDescriptor(long movieId, long userId, double ratingValue, long timestamp) {
        this.movieId = movieId;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.ratingTime = ofInstant(ofEpochMilli(timestamp), getDefault().toZoneId()).withNano(0);
    }
}
