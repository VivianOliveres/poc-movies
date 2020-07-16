package com.poc.movies.inventory;

import com.poc.movies.inventory.model.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InventoryApp.class})
@Sql(scripts = {"/data-mysql.sql"})
public class InventoryServiceTest {

    @Autowired
    private InventoryService service;

    @Test
    public void should_insert_then_getById_movie() {
        Movie movie = new Movie(1L, "Toy Story (1995)", Set.of("Adventure", "Animation", "Children", "Comedy", "Fantasy"));
        service.insert(movie);

        Movie returnedMovie = service.getByMovieId(movie.getId());
        assertThat(returnedMovie).isEqualTo(movie);
    }

    @Test
    public void should_not_update_existing_by_inserting_a_new_one() {
        Movie movie1 = new Movie(1L, "Toy Story (1995)", Set.of("Adventure", "Animation", "Children", "Comedy", "Fantasy"));
        service.insert(movie1);

        Movie movie2 = new Movie(1L, "Jumanji (1995)", Set.of("Adventure", "Children", "Fantasy"));
        service.insert(movie2);

        Movie returnedMovie = service.getByMovieId(1L);
        assertThat(returnedMovie).isEqualTo(movie1);
    }

    @Test
    public void should_insert_multiple_movies_and_retrieve_them() {
        Movie movie1 = new Movie(1L, "Toy Story (1995)", Set.of("Adventure", "Animation", "Children", "Comedy", "Fantasy"));
        Movie movie2 = new Movie(2L, "Jumanji (1995)", Set.of("Adventure", "Children", "Fantasy"));
        service.insert(List.of(movie1, movie2));

        Movie returnedMovie1 = service.getByMovieId(movie1.getId());
        assertThat(returnedMovie1).isEqualTo(movie1);

        Movie returnedMovie2 = service.getByMovieId(movie2.getId());
        assertThat(returnedMovie2).isEqualTo(movie2);

        List<Movie> returnedMovies = service.getByMovieIds(List.of(1L, 2L));
        assertThat(returnedMovies).containsOnly(movie1, movie2);
    }

    @Test
    public void should_insert_then_update_movie() {
        // GIVEN: movie inserted
        Movie movie = new Movie(1L, "Toy Story (1995)", Set.of("Adventure", "Animation", "Children", "Comedy", "Fantasy"));
        service.insert(List.of(movie));

        Movie returnedMovie = service.getByMovieId(movie.getId());
        assertThat(returnedMovie).isEqualTo(movie);

        // WHEN: update title
        Movie movieWithTitleUpdated = movie.withTitle("Toy Story");
        service.update(movieWithTitleUpdated);

        // THEN: title is updated
        returnedMovie = service.getByMovieId(movie.getId());
        assertThat(returnedMovie).isEqualTo(movieWithTitleUpdated);

        // WHEN: update category
        Movie movieWithCategoriesUpdated = movieWithTitleUpdated.withCategories(Set.of("Animation", "Children", "Comedy", "Fantasy"));
        service.update(movieWithCategoriesUpdated);

        // THEN: title and categories are updated
        returnedMovie = service.getByMovieId(movie.getId());
        assertThat(returnedMovie.getTitle()).isEqualTo(movieWithTitleUpdated.getTitle());
        assertThat(returnedMovie.getCategories()).isEqualTo(movieWithCategoriesUpdated.getCategories());
    }

}
