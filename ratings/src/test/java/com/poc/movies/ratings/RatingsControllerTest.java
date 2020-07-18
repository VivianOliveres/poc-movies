package com.poc.movies.ratings;

import com.poc.movies.ratings.model.Rating;
import org.assertj.core.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatingsApp.class)
@AutoConfigureMockMvc
@Sql(scripts = {"/clean-ratings-data.sql"})
public class RatingsControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final Rating RATING_1_2 = new Rating(1, 2, 3.5, 1112486027L);
    private static final Rating RATING_1_29 = new Rating(1, 29, 3.5, 1112484676L);
    private static final Rating RATING_1_32 = new Rating(1, 32, 3.5, 1112484819L);
    private static final Rating RATING_11_185 = new Rating(11, 185, 4.0, 1230859015L);

    @Test
    public void integration_test() throws Exception {
        test_rating_1_2();
        test_other_ratings();
    }

    /**
     * POST /ratings RATING_1_2
     * GET /ratings/movie/1
     * GET /ratings/user/2
     * GET /ratings/user/2/movie/1
     * GET /ratings/movie/1/user/2
     */
    private void test_rating_1_2() throws Exception {
        // POST json rating 1
        String jsonRating1 = "{\"movieId\":1,\"userId\":2,\"ratingValue\":3.5,\"ratingTime\":\"2005-04-03T01:53:47\"}";
        mvc.perform(post("/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRating1))
                .andExpect(status().isCreated());

        // GET rating 1 by movieId
        checkArray("/ratings/movie/1", jsonRating1);

        // GET rating 1 by userId
        checkArray("/ratings/user/2", jsonRating1);

        // GET rating 1 by userId and movieId
        checkStray("/ratings/user/2/movie/1", jsonRating1);

        // GET rating 1 by movieId and userId
        checkStray("/ratings/movie/1/user/2", jsonRating1);
    }

    /**
     * POST /ratings/bulk [RATING_1_29, RATING_1_32, RATING_11_185]
     *
     * GET /ratings/movie/1
     * GET /ratings/movie/11
     *
     * GET /ratings/user/29
     * GET /ratings/user/32
     * GET /ratings/user/185
     */
    private void test_other_ratings() throws Exception {
        // POST jsonRating_1_29, jsonRating_1_32, jsonRating_11_185
        String jsonRating_1_29 = "{\"movieId\":1,\"userId\":29,\"ratingValue\":3.5,\"ratingTime\":\"2005-04-03T01:31:16\"}";
        String jsonRating_1_32 = "{\"movieId\":1,\"userId\":32,\"ratingValue\":3.5,\"ratingTime\":\"2005-04-03T01:33:39\"}";
        String jsonRating_11_185 = "{\"movieId\":11,\"userId\":185,\"ratingValue\":4.0,\"ratingTime\":\"2009-01-02T02:16:55\"}";
        String jsonArrayRatings = "[" + jsonRating_1_29 + "," + jsonRating_1_32 + "," + jsonRating_11_185 + "]";
        mvc.perform(post("/ratings/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonArrayRatings))
                .andExpect(status().isCreated());

        // GET /ratings/movie/1
        String jsonRating_1_2 = "{\"movieId\":1,\"userId\":2,\"ratingValue\":3.5,\"ratingTime\":\"2005-04-03T01:53:47\"}";
        checkArray("/ratings/movie/1", jsonRating_1_2, jsonRating_1_29, jsonRating_1_32);

        // GET /ratings/movie/11
        checkArray("/ratings/movie/11", jsonRating_11_185);

        // GET /ratings/user/29
        checkArray("/ratings/user/29", jsonRating_1_29);

        // GET /ratings/user/32
        checkArray("/ratings/user/32", jsonRating_1_32);

        // GET /ratings/user/185
        checkArray("/ratings/user/185", jsonRating_11_185);
    }

    private void checkStray(String getUrl, String expectedJson) throws Exception {
        mvc.perform(get(getUrl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    private void checkArray(String getUrl, String... jsons) throws Exception {
        var expectedJson = buildJsonArray(jsons);
        checkStray(getUrl, expectedJson);
    }

    private String buildJsonArray(String... jsons) {
        return "[" + Strings.join(jsons).with(",") + "]";
    }
}
