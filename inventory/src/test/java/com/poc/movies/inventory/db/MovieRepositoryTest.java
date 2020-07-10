package com.poc.movies.inventory.db;

import com.poc.movies.inventory.InventoryApp;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InventoryApp.class})
public class MovieRepositoryTest extends AbstractRepositoryTest {

    @Test
    public void should_find_nothing_in_table_at_startup() {
        List<MovieEntity> entities = Lists.newArrayList(movieRepository.findAll());
        assertThat(entities).isEmpty();
    }

    @Test
    public void should_insert_movie() {
        var expectedMovie = new MovieEntity(1L, "The Fellowship of the Ring");

        long insertedId = movieRepository.insertOne(expectedMovie.getMovieId(), expectedMovie.getTitle());
        assertThat(insertedId).isEqualTo(expectedMovie.getMovieId());

        List<MovieEntity> entities = Lists.newArrayList(movieRepository.findAll());
        assertThat(entities).containsExactly(expectedMovie);
    }

    @Test
    public void should_update_existing_movie() {
        var expectedMovie = new MovieEntity(1L, "The Fellowship of the Ring");
        movieRepository.insertOne(expectedMovie.getMovieId(), expectedMovie.getTitle());

        var movieToUpdate = new MovieEntity(expectedMovie.getMovieId(), "The Two Towers");
        var updatedMovie = movieRepository.save(movieToUpdate);
        assertThat(updatedMovie).isEqualTo(movieToUpdate);

        List<MovieEntity> entities = Lists.newArrayList(movieRepository.findAll());
        assertThat(entities).containsExactly(movieToUpdate);
    }

    @Test
    public void should_delete_existing_movie() {
        var expectedMovie = new MovieEntity(1L, "The Fellowship of the Ring");
        movieRepository.insertOne(expectedMovie.getMovieId(), expectedMovie.getTitle());

        movieRepository.delete(expectedMovie);

        List<MovieEntity> entities = Lists.newArrayList(movieRepository.findAll());
        assertThat(entities).isEmpty();
    }
}
