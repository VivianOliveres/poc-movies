package com.poc.movies.ratings.commands;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class InsertRatingCommand {

    long movieId;

    long userId;

    double ratingValue;

    LocalDateTime ratingTime;
}
