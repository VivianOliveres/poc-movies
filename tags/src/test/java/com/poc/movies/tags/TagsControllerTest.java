package com.poc.movies.tags;

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
@SpringBootTest(classes = TagsApp.class)
@AutoConfigureMockMvc
@Sql(scripts = {"/clean-tags-data.sql"})
public class TagsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void integration_test() throws Exception {
        // POST tag 18_4141
        String jsonTag_18_4141 = "{\"movieId\":18,\"userId\":4141,\"tagName\":\"mark waters\",\"tagTime\":\"2009-04-24T20:19:40\"}";
        mvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTag_18_4141))
                .andExpect(status().isCreated());

        // GET tag 18_4141
        checkArray("/tags/movie/18", jsonTag_18_4141);
        checkArray("/tags/movie/18/user/4141", jsonTag_18_4141);
        checkArray("/tags/user/4141", jsonTag_18_4141);
        checkArray("/tags/user/4141/movie/18", jsonTag_18_4141);

        // POST tag 65_208 && 65_353
        String jsonTag_65_208 = "{\"movieId\":65,\"userId\":208,\"tagName\":\"dark hero\",\"tagTime\":\"2013-05-10T03:41:18\"}";
        String jsonTag_65_353 = "{\"movieId\":65,\"userId\":353,\"tagName\":\"dark hero\",\"tagTime\":\"2013-05-10T03:41:19\"}";
        mvc.perform(post("/tags/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJsonArray(jsonTag_65_208, jsonTag_65_353)))
                .andExpect(status().isCreated());

        // GET tags
        checkArray("/tags/movie/65", jsonTag_65_208, jsonTag_65_353);
        checkArray("/tags/movie/65/user/208", jsonTag_65_208);
        checkArray("/tags/movie/65/user/353", jsonTag_65_353);
        checkArray("/tags/user/208", jsonTag_65_208);
        checkArray("/tags/user/353", jsonTag_65_353);
        checkArray("/tags/user/208/movie/65", jsonTag_65_208);
        checkArray("/tags/user/353/movie/65", jsonTag_65_353);

        // POST tag TAG_318_260_1 && TAG_318_260_1
        String jsonTag_318_260_1 = "{\"movieId\":318,\"userId\":260,\"tagName\":\"1970s\",\"tagTime\":\"2015-02-20T23:42:49\"}";
        String jsonTag_318_260_2 = "{\"movieId\":318,\"userId\":260,\"tagName\":\"fantasy\",\"tagTime\":\"2015-02-20T23:42:49\"}";
        mvc.perform(post("/tags/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJsonArray(jsonTag_318_260_1, jsonTag_318_260_2)))
                .andExpect(status().isCreated());

        // GET tags
        checkArray("/tags/movie/318", jsonTag_318_260_1, jsonTag_318_260_2);
        checkArray("/tags/movie/318/user/260", jsonTag_318_260_2, jsonTag_318_260_1);
        checkArray("/tags/user/260", jsonTag_318_260_2, jsonTag_318_260_1);
        checkArray("/tags/user/260/movie/318", jsonTag_318_260_2, jsonTag_318_260_1);
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
