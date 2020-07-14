package com.poc.movies.ratings.db;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Value
@Table("Ratings")
public class RatingEntity {

    @Id
    @Column("rating_id")
    Long id;

    @Column("movie_id")
    long movieId;

    @Column("user_id")
    long userId;

    @Column("value")
    double ratingValue;

    @Column("ratingTime")
    LocalDateTime ratingTime;
}
