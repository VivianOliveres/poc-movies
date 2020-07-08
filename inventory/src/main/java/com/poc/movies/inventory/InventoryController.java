package com.poc.movies.inventory;

import com.poc.movies.inventory.model.Movie;
import com.poc.movies.inventory.requests.InsertMovieCommand;
import org.springframework.web.bind.annotation.*;

@RestController
public class InventoryController {

    private InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/inventory/movie/{id}")
    public Movie getById(@PathVariable long id) {
        return service.getById(id);
    }

    @PostMapping("/inventory/movie")
    public Movie insertOne(@RequestBody InsertMovieCommand dto) {
        Movie movie = new Movie(dto.getId(), dto.getTitle(), dto.getGenres());
        return service.insert(movie);
    }

}
