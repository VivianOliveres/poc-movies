package com.poc.movies.links;

import com.poc.movies.links.model.MovieLinks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LinksApp.class})
@Sql(scripts = {"/delete-all.sql"})
public class LinksServiceTest {

    private static final MovieLinks LINK_1 = new MovieLinks(1L, "0114709", 862L);
    private static final MovieLinks LINK_2 = new MovieLinks(2L, "0113497", 8844L);
    private static final MovieLinks LINK_3 = new MovieLinks(3L, "0113228", 15602L);

    @Autowired
    private LinksService service;

    @Test
    public void should_insert_link() {
        MovieLinks insertedLinks = service.insertOne(LINK_1);

        assertThat(insertedLinks).isNotNull();
        assertThat(insertedLinks).isEqualTo(LINK_1);
    }

    @Test
    public void should_getOne_return_links() {
        service.insertOne(LINK_1);

        Optional<MovieLinks> maybeLinks = service.getOne(LINK_1.getMovieId());
        assertThat(maybeLinks).isNotNull();
        assertThat(maybeLinks).isNotEmpty();

        var links = maybeLinks.get();
        assertThat(links.getMovieId()).isEqualTo(LINK_1.getMovieId());
        assertThat(links.getImdbId()).isEqualTo(LINK_1.getImdbId());
        assertThat(links.getTmdbId()).isEqualTo(LINK_1.getTmdbId());
    }

    @Test
    public void should_retrieve_imdb_links() {
        service.insertOne(LINK_1);

        MovieLinks imdbLinks = service.getImdb(LINK_1.getMovieId());
        assertThat(imdbLinks).isNotNull();
        assertThat(imdbLinks).isEqualTo(new MovieLinks(LINK_1.getMovieId(), LINK_1.getImdbId(), null));
    }

    @Test
    public void should_retrieve_tmdb_links() {
        service.insertOne(LINK_1);

        MovieLinks tmdbLinks = service.getTmdb(LINK_1.getMovieId());
        assertThat(tmdbLinks).isNotNull();
        assertThat(tmdbLinks).isEqualTo(new MovieLinks(LINK_1.getMovieId(), null, LINK_1.getTmdbId()));
    }

    @Test
    public void should_insert_many_links_then_retrieve_them_all() {
        service.insertOne(LINK_1);
        service.insertOne(LINK_2);
        service.insertOne(LINK_3);

        List<MovieLinks> allLinks = service.getBulk(List.of(LINK_1.getMovieId(), LINK_2.getMovieId()));
        assertThat(allLinks).hasSize(2);
        assertThat(allLinks).containsOnly(LINK_1, LINK_2);
    }

    @Test
    public void should_insert_bulk_links_then_retrieve_them_all() {
        List<MovieLinks> inserted = service.insertBulk(List.of(LINK_1, LINK_2));
        assertThat(inserted).hasSize(2);
        assertThat(inserted).containsOnly(LINK_1, LINK_2);

        List<MovieLinks> allLinks = service.getBulk(List.of(LINK_1.getMovieId(), LINK_2.getMovieId()));
        assertThat(allLinks).hasSize(2);
        assertThat(allLinks).containsOnly(LINK_1, LINK_2);
    }

    @Test
    public void should_getOne_return_empty_for_unknown_movieId() {
        Optional<MovieLinks> returned = service.getOne(3L);
        assertThat(returned).isEmpty();
    }

    @Test
    public void should_deleteOne() {
        service.insertOne(LINK_1);

        Optional<MovieLinks> maybeDeletedLink = service.deleteOne(LINK_1.getMovieId());
        assertThat(maybeDeletedLink).isNotEmpty();
        assertThat(maybeDeletedLink.get()).isEqualTo(LINK_1);

        Optional<MovieLinks> maybeReturnedLink = service.getOne(LINK_1.getMovieId());
        assertThat(maybeReturnedLink).isEmpty();
    }

    @Test
    public void should_deleteBulk() {
        service.insertBulk(List.of(LINK_1, LINK_2, LINK_3));

        List<MovieLinks> maybeDeletedLink = service.deleteBulk(List.of(LINK_1.getMovieId(), LINK_2.getMovieId()));
        assertThat(maybeDeletedLink).hasSize(2);
        assertThat(maybeDeletedLink).containsOnly(LINK_1, LINK_2);

        Optional<MovieLinks> maybeReturnedLink1 = service.getOne(LINK_1.getMovieId());
        assertThat(maybeReturnedLink1).isEmpty();

        Optional<MovieLinks> maybeReturnedLink2 = service.getOne(LINK_2.getMovieId());
        assertThat(maybeReturnedLink2).isEmpty();

        Optional<MovieLinks> maybeReturnedLink3 = service.getOne(LINK_3.getMovieId());
        assertThat(maybeReturnedLink3).isNotEmpty();
        assertThat(maybeReturnedLink3.get()).isEqualTo(LINK_3);
    }

    @Test
    public void should_update_imbd_to_new_value() {
        service.insertOne(LINK_1);

        MovieLinks toUpdateLink = LINK_1.withImdbId("imdb_new_value");
        Optional<MovieLinks> updatedLink = service.updateOne(toUpdateLink);
        assertThat(updatedLink).isNotEmpty();
        assertThat(updatedLink.get()).isEqualTo(toUpdateLink);

        Optional<MovieLinks> maybeReturnedLink = service.getOne(LINK_1.getMovieId());
        assertThat(maybeReturnedLink).isNotEmpty();
        assertThat(maybeReturnedLink.get()).isEqualTo(toUpdateLink);
    }

    @Test
    public void should_update_tmbd_to_new_value() {
        service.insertOne(LINK_1);

        MovieLinks toUpdateLink = LINK_1.withTmdbId(123456789L);
        Optional<MovieLinks> updatedLink = service.updateOne(toUpdateLink);
        assertThat(updatedLink).isNotEmpty();
        assertThat(updatedLink.get()).isEqualTo(toUpdateLink);

        Optional<MovieLinks> maybeReturnedLink = service.getOne(LINK_1.getMovieId());
        assertThat(maybeReturnedLink).isNotEmpty();
        assertThat(maybeReturnedLink.get()).isEqualTo(toUpdateLink);
    }

    @Test
    public void should_update_imbd_to_null() {
        service.insertOne(LINK_1);

        MovieLinks toUpdateLink = LINK_1.withImdbId(null);
        Optional<MovieLinks> updatedLink = service.updateOne(toUpdateLink);
        assertThat(updatedLink).isNotEmpty();
        assertThat(updatedLink.get()).isEqualTo(toUpdateLink);
        assertThat(updatedLink.get().getImdbId()).isNull();

        Optional<MovieLinks> maybeReturnedLink = service.getOne(LINK_1.getMovieId());
        assertThat(maybeReturnedLink).isNotEmpty();
        assertThat(maybeReturnedLink.get()).isEqualTo(toUpdateLink);
        assertThat(maybeReturnedLink.get().getImdbId()).isNull();
    }

    @Test
    public void should_update_tmbd_to_null() {
        service.insertOne(LINK_1);

        MovieLinks toUpdateLink = LINK_1.withTmdbId(null);
        Optional<MovieLinks> updatedLink = service.updateOne(toUpdateLink);
        assertThat(updatedLink).isNotEmpty();
        assertThat(updatedLink.get()).isEqualTo(toUpdateLink);
        assertThat(updatedLink.get().getTmdbId()).isNull();

        Optional<MovieLinks> maybeReturnedLink = service.getOne(LINK_1.getMovieId());
        assertThat(maybeReturnedLink).isNotEmpty();
        assertThat(maybeReturnedLink.get()).isEqualTo(toUpdateLink);
        assertThat(maybeReturnedLink.get().getTmdbId()).isNull();
    }

    @Test
    public void should_update_return_empty_for_unknown_movieId() {
        Optional<MovieLinks> maybeLink = service.updateOne(LINK_1);
        assertThat(maybeLink).isEmpty();
    }

}
