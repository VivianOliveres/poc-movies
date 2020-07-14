package com.poc.movies.ratings;

import com.poc.movies.ratings.model.Rating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RatingsApp.class})
@Sql(scripts = {"/clean-ratings-data.sql"})
public class RatingsServiceTest {

    private static final Rating RATING_1_2 = new Rating(1, 2, 3.5, 1112486027L);
    private static final Rating RATING_1_29 = new Rating(1, 29, 3.5, 1112484676L);
    private static final Rating RATING_1_32 = new Rating(1, 32, 3.5, 1112484819L);
    private static final Rating RATING_11_185 = new Rating(11, 185, 4.0, 1230859015L);

    @Autowired
    private RatingsService service;

    @Test
    public void should_throws_IllegalArgumentException_when_get_unknown_rating() {
        assertThrows(IllegalArgumentException.class, () -> service.getRating(RATING_1_2.getMovieId(), RATING_1_2.getMovieId()));
    }

    @Test
    public void should_insert_one_rating_then_retrieve_it() {
        Rating inserted = service.insert(RATING_1_2);
        assertThat(inserted).isEqualTo(RATING_1_2);

        Rating returnedRating = service.getRating(RATING_1_2.getMovieId(), RATING_1_2.getUserId());
        assertThat(returnedRating).isEqualTo(RATING_1_2);
    }

    @Test
    public void should_insert_many_ratings() {
        service.insertMany(List.of(RATING_1_2, RATING_1_29, RATING_1_32));

        Rating returnedRating1 = service.getRating(RATING_1_2.getMovieId(), RATING_1_2.getUserId());
        assertThat(returnedRating1).isEqualTo(RATING_1_2);

        Rating returnedRating2 = service.getRating(RATING_1_29.getMovieId(), RATING_1_29.getUserId());
        assertThat(returnedRating2).isEqualTo(RATING_1_29);

        Rating returnedRating3 = service.getRating(RATING_1_32.getMovieId(), RATING_1_32.getUserId());
        assertThat(returnedRating3).isEqualTo(RATING_1_32);
    }

    @Test
    public void should_retrieve_ratings_by_movieId() {
        service.insertMany(List.of(RATING_1_2, RATING_1_29, RATING_1_32, RATING_11_185));

        List<Rating> returnedRatings = service.getRatingsByMovieId(RATING_1_2.getMovieId());
        assertThat(returnedRatings).containsOnly(RATING_1_2, RATING_1_29, RATING_1_32);

        returnedRatings = service.getRatingsByMovieId(RATING_11_185.getMovieId());
        assertThat(returnedRatings).containsOnly(RATING_11_185);
    }

    @Test
    public void should_retrieve_ratings_by_userId() {
        service.insertMany(List.of(RATING_1_2, RATING_1_29, RATING_1_32, RATING_11_185));

        List<Rating> returnedRatings = service.getRatingsByUserId(RATING_1_2.getUserId());
        assertThat(returnedRatings).containsOnly(RATING_1_2);
    }

}
