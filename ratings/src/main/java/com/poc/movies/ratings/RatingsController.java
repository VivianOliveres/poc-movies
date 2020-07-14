package com.poc.movies.ratings;

import com.poc.movies.ratings.commands.InsertRatingCommand;
import com.poc.movies.ratings.model.Rating;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/ratings")
public class RatingsController {

    private RatingsService service;

    public RatingsController(RatingsService service) {
        this.service = service;
    }

    @GetMapping("/movie/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Rating> getRatingsByMovieId(@PathVariable long movieId) {
        return service.getRatingsByMovieId(movieId);
    }

    @GetMapping("/movie/{movieId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Rating getRatingByMovie(@PathVariable long movieId, @PathVariable long userId) {
        return service.getRating(movieId, userId);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Rating> getRatingsByUserId(@PathVariable long userId) {
        return service.getRatingsByUserId(userId);
    }

    @GetMapping("/user/{userId}/movie/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public Rating getRatingByUser(@PathVariable long movieId, @PathVariable long userId) {
        return service.getRating(movieId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void insertOne(@RequestBody InsertRatingCommand dto) {
        Rating rating = new Rating(dto.getMovieId(), dto.getUserId(), dto.getRatingValue(), dto.getRatingTime());
        service.insert(rating);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertMany(@RequestBody List<InsertRatingCommand> dtos) {
        List<Rating> ratingsToInsert = dtos.stream()
                .map(dto -> new Rating(dto.getMovieId(), dto.getUserId(), dto.getRatingValue(), dto.getRatingTime()))
                .collect(toList());
        service.insertMany(ratingsToInsert);
    }

    @PostMapping("/user/{userId}/movie/{movieId}/{ratingValue}")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertOne(@PathVariable long movieId, @PathVariable long userId, @PathVariable double ratingValue) {
        Rating rating = new Rating(movieId, userId, ratingValue, LocalDateTime.now());
        service.insert(rating);
    }

}
