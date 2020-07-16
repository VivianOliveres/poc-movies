package com.poc.movies.ratings;

import com.poc.movies.ratings.model.Rating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        // POST movie 1
        String jsonRating1 = "{\"movieId\":1,\"userId\":2,\"ratingValue\":3.5,\"ratingTime\":[1970,1,13,22,1,26]}";
        mvc.perform(post("/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRating1))
                .andExpect(status().isCreated());
    }
}
