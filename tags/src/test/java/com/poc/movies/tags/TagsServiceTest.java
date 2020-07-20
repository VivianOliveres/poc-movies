package com.poc.movies.tags;

import static com.poc.movies.tags.TagsData.*;
import com.poc.movies.tags.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TagsApp.class})
@Sql(scripts = {"/clean-tags-data.sql"})
public class TagsServiceTest {

    @Autowired
    private TagsService service;

    @Test
    public void should_getTagsByMovieId_return_nothing_when_movie_is_unknown() {
        List<Tag> tagsByMovieId = service.getTagsByMovieId(123);
        assertThat(tagsByMovieId).isEmpty();
    }

    @Test
    public void should_insert_then_getTagsByMovieId() {
        service.insert(TAG_18_4141);

        List<Tag> tagsByMovieId = service.getTagsByMovieId(TAG_18_4141.getMovieId());
        assertThat(tagsByMovieId).containsOnly(TAG_18_4141);
    }

    @Test
    public void should_insertMany_then_getTagsByMovieId() {
        service.insertMany(List.of(TAG_18_4141, TAG_65_208, TAG_65_353, TAG_65_521));

        List<Tag> tags = service.getTagsByMovieId(TAG_18_4141.getMovieId());
        assertThat(tags).containsOnly(TAG_18_4141);

        tags = service.getTagsByMovieId(TAG_65_208.getMovieId());
        assertThat(tags).containsOnly(TAG_65_208, TAG_65_353, TAG_65_521);
    }

    @Test
    public void should_getTagsByUserId_return_nothing_when_user_is_unknown() {
        List<Tag> tagsByMovieId = service.getTagsByUserId(123);
        assertThat(tagsByMovieId).isEmpty();
    }

    @Test
    public void should_insert_then_getTagsByUserId() {
        service.insert(TAG_18_4141);

        List<Tag> tagsByMovieId = service.getTagsByUserId(TAG_18_4141.getUserId());
        assertThat(tagsByMovieId).containsOnly(TAG_18_4141);
    }

    @Test
    public void should_insertMany_then_getTagsByUserId() {
        service.insertMany(List.of(TAG_318_260_1, TAG_318_260_2));

        List<Tag> tags = service.getTagsByUserId(TAG_318_260_1.getUserId());
        assertThat(tags).containsOnly(TAG_318_260_1, TAG_318_260_2);

        tags = service.getTagsByUserId(TAG_65_208.getUserId());
        assertThat(tags).isEmpty();
    }

    @Test
    public void should_getTagsByMovieAndUser_return_nothing_when_user_is_unknown() {
        List<Tag> tagsByMovieId = service.getTagsByMovieAndUser(123, 456);
        assertThat(tagsByMovieId).isEmpty();
    }

    @Test
    public void should_insert_then_getTagsByMovieAndUser() {
        service.insert(TAG_18_4141);

        List<Tag> tagsByMovieId = service.getTagsByMovieAndUser(TAG_18_4141.getMovieId(), TAG_18_4141.getUserId());
        assertThat(tagsByMovieId).containsOnly(TAG_18_4141);
    }

    @Test
    public void should_insertMany_then_getTagsByMovieAndUser() {
        service.insertMany(List.of(TAG_18_4141, TAG_65_208, TAG_65_353, TAG_65_521));

        List<Tag> tags = service.getTagsByMovieAndUser(TAG_18_4141.getMovieId(), TAG_18_4141.getUserId());
        assertThat(tags).containsOnly(TAG_18_4141);

        tags = service.getTagsByMovieAndUser(TAG_65_208.getMovieId(), TAG_65_208.getUserId());
        assertThat(tags).containsOnly(TAG_65_208);

        tags = service.getTagsByMovieAndUser(TAG_65_353.getMovieId(), TAG_65_353.getUserId());
        assertThat(tags).containsOnly(TAG_65_353);

        tags = service.getTagsByMovieAndUser(TAG_65_521.getMovieId(), TAG_65_521.getUserId());
        assertThat(tags).containsOnly(TAG_65_521);
    }

}
