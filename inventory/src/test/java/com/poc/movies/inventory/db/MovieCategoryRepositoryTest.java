package com.poc.movies.inventory.db;

import com.poc.movies.inventory.InventoryApp;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InventoryApp.class})
public class MovieCategoryRepositoryTest extends AbstractRepositoryTest {

    private static final MovieEntity MOVIE = new MovieEntity(1, "The Fellowship of the Ring");

    private static CategoryEntity CATEGORY = new CategoryEntity(null, "Action");

    @Before
    public void before() {
        super.before();

        assertThat(movieCategoryRepository.count()).isZero();

        movieRepository.insertOne(MOVIE.getMovieId(), MOVIE.getTitle());
        assertThat(movieRepository.count()).isEqualTo(1);
        assertThat(movieRepository.findById(MOVIE.getMovieId())).isEqualTo(Optional.of(MOVIE));

        categoryRepository.insertIgnoreOne(CATEGORY.getCategoryName());
        CATEGORY = categoryRepository.findByCategoryName(CATEGORY.getCategoryName());
        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(categoryRepository.findById(CATEGORY.getCategoryId())).isEqualTo(Optional.of(CATEGORY));
    }

    @Test
    public void should_insert_entity() {
        var insertedEntity = movieCategoryRepository.save(new MovieCategoryEntity(null, MOVIE.getMovieId(), CATEGORY.getCategoryId()));

        assertThat(insertedEntity).isNotNull();
        assertThat(insertedEntity.getMovieCategoryId()).isNotNull();
        assertThat(insertedEntity.getMovieId()).isEqualTo(MOVIE.getMovieId());
        assertThat(insertedEntity.getCategoryId()).isEqualTo(CATEGORY.getCategoryId());

        var entities = Lists.newArrayList(movieCategoryRepository.findAll());
        assertThat(entities).containsExactly(insertedEntity);
    }

    @Test
    public void should_delete_existing_category() {
        var insertedEntity = movieCategoryRepository.save(new MovieCategoryEntity(null, MOVIE.getMovieId(), CATEGORY.getCategoryId()));

        movieCategoryRepository.delete(insertedEntity);

        var entities = Lists.newArrayList(movieCategoryRepository.findAll());
        assertThat(entities).isEmpty();
    }

    @Test
    public void should_findAllByMovieId_return_expected_values() {
        movieCategoryRepository.save(new MovieCategoryEntity(null, MOVIE.getMovieId(), CATEGORY.getCategoryId()));

        Set<CategoryEntity> categories = categoryRepository.findAllByMovieId(MOVIE.getMovieId());
        assertThat(categories).containsExactly(CATEGORY);
    }

    @Test
    public void should_findAllByMovieId_return_empty_for_unknown_values() {
        movieCategoryRepository.save(new MovieCategoryEntity(null, MOVIE.getMovieId(), CATEGORY.getCategoryId()));

        Set<CategoryEntity> categories = categoryRepository.findAllByMovieId(123456789L);
        assertThat(categories).isEmpty();
    }

    @Test
    public void should_findAllByCategoryName_return_expected_values() {
        movieCategoryRepository.save(new MovieCategoryEntity(null, MOVIE.getMovieId(), CATEGORY.getCategoryId()));

        Set<String> categoryNames = Set.of(CATEGORY.getCategoryName());
        Set<CategoryEntity> categories = categoryRepository.findAllByCategoryName(categoryNames);
        assertThat(categories).containsExactly(CATEGORY);
    }

    @Test
    public void should_findAllByCategoryName_return_empty_for_unknown_values() {
        movieCategoryRepository.save(new MovieCategoryEntity(null, MOVIE.getMovieId(), CATEGORY.getCategoryId()));

        Set<String> categoryNames = Set.of("Unknown category");
        Set<CategoryEntity> categories = categoryRepository.findAllByCategoryName(categoryNames);
        assertThat(categories).isEmpty();
    }
}
