package com.poc.movies.inventory;

import com.poc.movies.inventory.model.Movie;
import com.poc.movies.inventory.requests.InsertMovieCommand;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class InventoryController {

    private InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/inventory/movie/{id}")
    public Movie getById(@PathVariable long id) {
        return service.getByMovieId(id);
    }

    @PostMapping("/inventory/movie")
    public void insertOne(@RequestBody InsertMovieCommand dto) {
        Movie movie = new Movie(dto.getId(), dto.getTitle(), dto.getGenres());
        service.insert(movie);
    }

    @PostMapping("/inventory/movies")
    public void insertMany(@RequestBody List<InsertMovieCommand> cmd) {
        List<Movie> movies = cmd.stream()
                .map(dto -> new Movie(dto.getId(), dto.getTitle(), dto.getGenres()))
                .collect(toList());
        service.insert(movies);
    }

    @GetMapping("/inventory/movies")
    public List<Movie> getMany(@RequestBody List<Long> ids) {
        return service.getByMovieIds(ids);
    }

}
