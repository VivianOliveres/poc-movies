package com.poc.movies.loader.inventory;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MainMovieLoaderTest {

    private MainMovieLoader loader = new MainMovieLoader();

    @Test
    public void should_extract_movie_from_regular_csv_line() {
        String csvLine = "1,Toy Story (1995),Adventure|Animation|Children|Comedy|Fantasy";

        MovieDescriptor desc = loader.extractMovie(csvLine);

        assertThat(desc).isNotNull();
        assertThat(desc.getId()).isEqualTo(1L);
        assertThat(desc.getTitle()).isEqualTo("Toy Story (1995)");
        assertThat(desc.getCategories()).containsOnly("Adventure", "Animation", "Children", "Comedy", "Fantasy");
    }

    @Test
    public void should_extract_movie_from_csv_line_containing_quotes_in_title() {
        String csvLine = "3364,\"Asphalt Jungle, The (1950)\",Crime|Film-Noir";

        MovieDescriptor desc = loader.extractMovie(csvLine);

        assertThat(desc).isNotNull();
        assertThat(desc.getId()).isEqualTo(3364L);
        assertThat(desc.getTitle()).isEqualTo("Asphalt Jungle, The (1950)");
        assertThat(desc.getCategories()).containsOnly("Crime", "Film-Noir");
    }

    @Test
    public void should_extract_movie_from_csv_line_containing_coma_in_title() {
        String csvLine = "104091,\"Devil's Nightmare, The (Plus longue nuit du diable, La) (1971)\",Fantasy|Horror";

        MovieDescriptor desc = loader.extractMovie(csvLine);

        assertThat(desc).isNotNull();
        assertThat(desc.getId()).isEqualTo(104091L);
        assertThat(desc.getTitle()).isEqualTo("Devil's Nightmare, The (Plus longue nuit du diable, La) (1971)");
        assertThat(desc.getCategories()).containsOnly("Fantasy", "Horror");
    }
}
