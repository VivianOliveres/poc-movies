package com.poc.movies.links.db;

import com.poc.movies.links.model.MovieLinks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Repository
public class LinkFacadeRepository {

    private ImdbRepository imdbRepo;
    private TmdbRepository tmdbRepo;

    public LinkFacadeRepository(ImdbRepository imdbRepository, TmdbRepository tmdbRepository) {
        this.imdbRepo = imdbRepository;
        this.tmdbRepo = tmdbRepository;
    }

    @Transactional
    public MovieLinks save(MovieLinks links) {
        //TODO
        if (links.getMovieId() == 1533) log.info("save {}", links);

        long movieId = links.getMovieId();
        if (links.getImdbId() != null) {
            imdbRepo.insertIgnoreOne(movieId, links.getImdbId());
        }
        if (links.getTmdbId() != null) {
            var lines = tmdbRepo.insertIgnoreOne(movieId, links.getTmdbId());
            if (links.getMovieId() == 1533) log.info("saved {} lines with tmdb[{}]", lines, links.getTmdbId());
        }
        return findByMovieId(movieId).get();
    }

    @Transactional
    public List<MovieLinks> saveAll(List<MovieLinks> links) {
        links.forEach(this::save);
        return findAllByMovieIds(links.stream().map(MovieLinks::getMovieId).collect(toList()));
    }

    @Transactional
    public Optional<MovieLinks> update(MovieLinks links) {
        long movieId = links.getMovieId();
        var link = findByMovieId(movieId);
        if (link.isPresent()) {
            if (links.getImdbId() == null) {
                imdbRepo.deleteIgnoreOne(movieId);

            } else {
                imdbRepo.updateOne(movieId, links.getImdbId());
            }

            if (links.getTmdbId() == null) {
                tmdbRepo.deleteIgnoreOne(movieId);

            } else {
                tmdbRepo.updateOne(movieId, links.getTmdbId());
            }

            return findByMovieId(movieId);
        }

        return link;
    }

    public Optional<MovieLinks> findByMovieId(long movieId) {
        var maybeImdb = imdbRepo.findById(movieId).map(ImdbEntity::getImdbId);
        var maybeTmdb = tmdbRepo.findById(movieId).map(TmdbEntity::getTmdbId);
        if (maybeImdb.isEmpty() && maybeTmdb.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new MovieLinks(movieId, maybeImdb, maybeTmdb));
    }

    @Transactional
    public Optional<MovieLinks> deleteByMovieId(long movieId) {
        Optional<MovieLinks> maybeLink = findByMovieId(movieId);
        if (maybeLink.isPresent()) {
            var link = maybeLink.get();
            if (link.getImdbId() != null) {
                imdbRepo.deleteById(movieId);
            }
            if (link.getTmdbId() != null) {
                tmdbRepo.deleteById(movieId);
            }
        }

        return maybeLink;
    }

    public List<MovieLinks> findAllByMovieIds(List<Long> movieIds) {
        Map<Long, String> imdbsByMovieId = newArrayList(imdbRepo.findAllById(movieIds)).stream().collect(toMap(ImdbEntity::getMovieId, ImdbEntity::getImdbId));
        Map<Long, Long> tmdbsByMovieId = newArrayList(tmdbRepo.findAllById(movieIds)).stream().collect(toMap(TmdbEntity::getMovieId, TmdbEntity::getTmdbId));
        return movieIds.stream()
                .map(id -> new MovieLinks(id, imdbsByMovieId.get(id), tmdbsByMovieId.get(id)))
                .collect(toList());
    }

    public Optional<String> findImdb(long movieId) {
        return imdbRepo.findById(movieId).map(ImdbEntity::getImdbId);
    }

    public Optional<Long> findTmdb(long movieId) {
        return tmdbRepo.findById(movieId).map(TmdbEntity::getTmdbId);
    }

    @Transactional
    public List<MovieLinks> deleteAllByMovieId(List<Long> movieIds) {
        List<MovieLinks> links = findAllByMovieIds(movieIds);
        movieIds.forEach(this::deleteByMovieId);
        return links;
    }
}
