package com.poc.movies.links;

import com.poc.movies.links.model.MovieLinks;
import com.poc.movies.links.requests.InsertAllLinks;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/external")
public class LinksController {

    private LinksService service;

    public LinksController(LinksService service) {
        this.service = service;
    }

    @GetMapping("/link/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieLinks getOne(@PathVariable long id){
        Optional<MovieLinks> maybeLinks = service.getOne(id);
        return maybeLinks.orElseThrow(() -> new IllegalArgumentException("Unknown movieId[" + id + "]"));
    }

    @DeleteMapping("/link/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MovieLinks deleteOne(@PathVariable long id){
        Optional<MovieLinks> maybeLinks = service.deleteOne(id);
        return maybeLinks.orElseThrow(() -> new IllegalArgumentException("Unknown movieId[" + id + "]"));
    }

    @PostMapping("/link")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieLinks insertOne(@RequestBody InsertAllLinks request){
        MovieLinks links = new MovieLinks(request.getMovieId(), request.getImdbId(), request.getTmdbId());
        return service.insertOne(links);
    }

    @PutMapping("/link")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MovieLinks updateOne(@RequestBody InsertAllLinks request){
        MovieLinks links = new MovieLinks(request.getMovieId(), request.getImdbId(), request.getTmdbId());
        Optional<MovieLinks> maybeUpdatedLinks = service.updateOne(links);
        return maybeUpdatedLinks.orElseThrow(() -> new IllegalArgumentException("Unknown movieId[" + request.getMovieId() + "]"));
    }

    @GetMapping("/link/{id}/imdb")
    @ResponseStatus(HttpStatus.OK)
    public MovieLinks getOneImdb(@PathVariable long id) {
        return service.getImdb(id);
    }

    @GetMapping("/link/{id}/tmdb")
    @ResponseStatus(HttpStatus.OK)
    public MovieLinks getOneTmdb(@PathVariable long id) {
        return service.getTmdb(id);
    }

    @GetMapping("/links")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieLinks> getBulk(@RequestBody List<Long> movieIds){
        return service.getBulk(movieIds);
    }

    @PostMapping("/links")
    @ResponseStatus(HttpStatus.CREATED)
    public List<MovieLinks> insertBulk(@RequestBody List<InsertAllLinks> requests){
        var linksToInsert = requests.stream().map(request -> new MovieLinks(request.getMovieId(), request.getImdbId(), request.getTmdbId())).collect(toList());
        return service.insertBulk(linksToInsert);
    }

    @DeleteMapping("/links")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<MovieLinks> deleteBulk(@RequestBody List<Long> movieIds){
        return service.deleteBulk(movieIds);
    }

}
