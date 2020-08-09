package com.poc.movies.batch.tags.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.util.TimeZone.getDefault;

@Data
@NoArgsConstructor
public class TagsDescriptor {

    long movieId;

    long userId;

    String tagName;

    LocalDateTime tagTime;

    public TagsDescriptor(long movieId, long userId, String tagName, long timestamp) {
        this(movieId, userId, tagName, ofInstant(ofEpochMilli(timestamp * 1000), getDefault().toZoneId()).withNano(0));
    }

    public TagsDescriptor(long movieId, long userId, String tagName, LocalDateTime tagTime) {
        this.movieId = movieId;
        this.userId = userId;
        this.tagName = tagName.toLowerCase();
        this.tagTime = tagTime;
    }
}
