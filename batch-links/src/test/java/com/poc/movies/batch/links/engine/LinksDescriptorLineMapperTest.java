package com.poc.movies.batch.links.engine;

import com.poc.movies.batch.links.model.LinksDescriptor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LinksDescriptorLineMapperTest {

    private LinksDescriptorLineMapper mapper = new LinksDescriptorLineMapper();

    @Test
    public void should_parse_null_tmdb() {
        String line = "142,0094878,";
        LinksDescriptor result = mapper.mapLine(line, 142);
        LinksDescriptor expected = new LinksDescriptor(142, "0094878", null);
        assertThat(result).isEqualTo(expected);
    }

}
