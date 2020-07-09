package com.poc.movies.inventory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApp.class)
@AutoConfigureMockMvc
@Sql(scripts = {"/data-mysql.sql"})
public class InventoryControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Long test with different steps: <br>
     * <p>
     * - POST movie1 <br>
     * - GET movie1 <br>
     * <p>
     * - POST movie2 <br>
     * - GET movie2 <br>
     * <p>
     * - GET movies 1 & 2 <br>
     * <p>
     * - POST movies 3 & 4 <br>
     * - GET movies 3 & 4 <br>
     * <p>
     * - PUT movie1 (update title) <br>
     * - GET movie1 (check title) <br>
     * <p>
     * - PUT movie1 (update categories) <br>
     * - GET movie1 (check categories) <br>
     */
    @Test
    public void integration_test() throws Exception {
        // POST movie 1
        String jsonMovie1 = "{\"id\": 1, \"title\": \"Toy Story (1995)\", \"categories\": [\"adventure\", \"animation\", \"children\", \"comedy\", \"fantasy\"]}";
        mvc.perform(post("/inventory/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMovie1))
                .andExpect(status().isCreated());

        // GET movie 1
        mvc.perform(get("/inventory/movie/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(jsonMovie1));

        // POST movie 2
        String jsonMovie2 = "{\"id\": 2, \"title\": \"Jumanji (1995)\", \"categories\": [\"adventure\", \"children\", \"fantasy\"]}";
        mvc.perform(post("/inventory/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMovie2))
                .andExpect(status().isCreated());

        // GET movie 2
        mvc.perform(get("/inventory/movie/2"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(jsonMovie2));

        // GET movies [1,2]
        var jsonMovies12 = "[{\"id\":1,\"title\":\"Toy Story (1995)\",\"categories\":[\"adventure\",\"fantasy\",\"children\",\"comedy\",\"animation\"]},{\"id\":2,\"title\":\"Jumanji (1995)\",\"categories\":[\"adventure\",\"fantasy\",\"children\"]}]";
        mvc.perform(get("/inventory/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1,2]"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(jsonMovies12));

        // POST movies [3,4]
        var jsonMovies34 = "[{\"id\": 3, \"title\": \"Grumpier Old Men (1995)\", \"categories\": [\"romance\",\"comedy\"]}, {\"id\": 4, \"title\": \"Waiting to Exhale (1995)\", \"categories\": [\"romance\",\"drama\",\"comedy\"]}]";
        mvc.perform(post("/inventory/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMovies34))
                .andExpect(status().isCreated());

        // GET movies [3,4]
        mvc.perform(get("/inventory/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[3,4]"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(jsonMovies34));

        // PUT movie 1 (remove year in title)
        String jsonMovie1Updated = "{\"id\": 1, \"title\": \"Toy Story\", \"categories\": [\"adventure\", \"animation\", \"children\", \"comedy\", \"fantasy\"]}";
        mvc.perform(put("/inventory/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMovie1Updated))
                .andExpect(status().isOk());

        // GET movie 1
        mvc.perform(get("/inventory/movie/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(jsonMovie1Updated));

        // PUT movie 1 (remove adventure in categories)
        String jsonMovie1Updated2 = "{\"id\": 1, \"title\": \"Toy Story\", \"categories\": [\"animation\", \"children\", \"comedy\", \"fantasy\"]}";
        mvc.perform(put("/inventory/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMovie1Updated2))
                .andExpect(status().isOk());

        // GET movie 1
        mvc.perform(get("/inventory/movie/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(jsonMovie1Updated2));

    }
}
