package com.poc.movies.inventory;

import com.poc.movies.inventory.exceptions.InsertException;
import com.poc.movies.inventory.model.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InventoryApp.class})
@Sql(scripts = {"/data-mysql.sql"})
public class InventoryServiceTest {

    @Autowired
    private InventoryService service;

    @Test
    public void should_insert_then_getById_movie() {
        Movie movie = new Movie(1L, "Toy Story (1995)", Set.of("Adventure", "Animation", "Children", "Comedy", "Fantasy"));
        Movie insertedMovie = service.insert(movie);
        assertThat(insertedMovie).isEqualTo(movie);

        Movie returnedMovie = service.getById(movie.getId());
        assertThat(returnedMovie).isEqualTo(movie);
    }

    @Test
    public void should_not_update_existing_by_inserting_a_new_one() {
        Movie movie1 = new Movie(1L, "Toy Story (1995)", Set.of("Adventure", "Animation", "Children", "Comedy", "Fantasy"));
        service.insert(movie1);

        Movie movie2 = new Movie(1L, "Jumanji (1995)", Set.of("Adventure", "Children", "Fantasy"));
        assertThrows(InsertException.class, () -> service.insert(movie2));
    }

}
