package com.poc.movies.ratings.model;

import lombok.Value;

import java.time.LocalDateTime;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.util.TimeZone.getDefault;

@Value
public class Rating {

    long movieId;

    long userId;

    double ratingValue;

    LocalDateTime ratingTime;

    public Rating(long movieId, long userId, double ratingValue, LocalDateTime ratingTime) {
        this.movieId = movieId;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.ratingTime = ratingTime;
    }

    public Rating(long movieId, long userId, double ratingValue, long timestamp) {
        this.movieId = movieId;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.ratingTime = ofInstant(ofEpochMilli(timestamp * 1000), getDefault().toZoneId()).withNano(0);
    }
}
