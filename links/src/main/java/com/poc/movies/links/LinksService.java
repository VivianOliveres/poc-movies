package com.poc.movies.links;

import com.poc.movies.links.db.LinkFacadeRepository;
import com.poc.movies.links.model.MovieLinks;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinksService {

    private LinkFacadeRepository repo;

    public LinksService(LinkFacadeRepository repo) {
        this.repo = repo;
    }

    public Optional<MovieLinks> getOne(long movieId) {
        return repo.findByMovieId(movieId);
    }

    public Optional<MovieLinks> deleteOne(long movieId) {
        return repo.deleteByMovieId(movieId);
    }

    public MovieLinks getImdb(long movieId) {
        var links = new MovieLinks(movieId);
        Optional<String> maybeImdb = repo.findImdb(movieId);
        return maybeImdb.map(links::withImdbId).orElse(links);
    }

    public MovieLinks getTmdb(long movieId) {
        var links = new MovieLinks(movieId);
        Optional<Long> maybeTmdb = repo.findTmdb(movieId);
        return maybeTmdb.map(links::withTmdbId).orElse(links);
    }

    public MovieLinks insertOne(MovieLinks links) {
        return repo.save(links);
    }

    public Optional<MovieLinks> updateOne(MovieLinks links) {
        return repo.update(links);
    }

    public List<MovieLinks> insertBulk(List<MovieLinks> links) {
        return repo.saveAll(links);
    }

    public List<MovieLinks> getBulk(List<Long> movieIds) {
        return repo.findAllByMovieIds(movieIds);
    }

    public List<MovieLinks> deleteBulk(List<Long> movieIds) {
        return repo.deleteAllByMovieId(movieIds);
    }
}
