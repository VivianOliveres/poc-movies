package com.poc.movies.inventory;

import com.poc.movies.inventory.model.Movie;
import com.poc.movies.inventory.requests.InsertMovieCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/movie/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Movie getById(@PathVariable long id) {
        return service.getByMovieId(id);
    }

    @PostMapping("/movie")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertOne(@RequestBody InsertMovieCommand dto) {
        Movie movie = new Movie(dto.getId(), dto.getTitle(), dto.getCategories());
        service.insert(movie);
    }

    @PutMapping("/movie")
    @ResponseStatus(HttpStatus.OK)
    public void updateOne(@RequestBody InsertMovieCommand dto) {
        Movie movie = new Movie(dto.getId(), dto.getTitle(), dto.getCategories());
        service.update(movie);
    }

    @PostMapping("/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertMany(@RequestBody List<InsertMovieCommand> cmd) {
        List<Movie> movies = cmd.stream()
                .map(dto -> new Movie(dto.getId(), dto.getTitle(), dto.getCategories()))
                .collect(toList());
        service.insert(movies);
    }

    @GetMapping("/movies")
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMany(@RequestBody List<Long> ids) {
        return service.getByMovieIds(ids);
    }

}
