package com.poc.movies.batch.inventory.engine;

import com.poc.movies.batch.inventory.model.MovieDescriptor;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieDescriptorProcessorTest {

    private MovieDescriptorProcessor processor = new MovieDescriptorProcessor();

    @Test
    public void should_replace_empty_categories() {
        MovieDescriptor item = new MovieDescriptor(131260L, "Rentun Ruusu (2001)", Set.of("(no genres listed)"));

        MovieDescriptor processedItem = processor.process(item);

        assertThat(processedItem.getId()).isEqualTo(item.getId());
        assertThat(processedItem.getTitle()).isEqualTo(item.getTitle());

        assertThat(processedItem.getCategories()).hasSize(0);
    }

    @Test
    public void should_lower_case_categories() {
        MovieDescriptor item = new MovieDescriptor(1L, "Toy Story (1995)", Set.of("Adventure", "Fantasy", "Animation", "Comedy", "Children"));

        MovieDescriptor processedItem = processor.process(item);

        assertThat(processedItem.getId()).isEqualTo(item.getId());
        assertThat(processedItem.getTitle()).isEqualTo(item.getTitle());

        assertThat(processedItem.getCategories()).containsOnly("adventure", "fantasy", "animation", "comedy", "children");
    }
}
