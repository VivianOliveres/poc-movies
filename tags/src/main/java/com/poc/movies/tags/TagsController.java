package com.poc.movies.tags;

import com.poc.movies.tags.commands.InsertTagCommand;
import com.poc.movies.tags.model.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/tags")
public class TagsController {

    private TagsService service;

    public TagsController(TagsService service) {
        this.service = service;
    }

    @GetMapping("/movie/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getTagsByMovieId(@PathVariable long movieId) {
        return service.getTagsByMovieId(movieId);
    }

    @GetMapping("/movie/{movieId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getTagsByMovieIdAndUserId(@PathVariable long movieId, @PathVariable long userId) {
        return service.getTagsByMovieAndUser(movieId, userId);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getTagsByUserId(@PathVariable long userId) {
        return service.getTagsByUserId(userId);
    }

    @GetMapping("/user/{userId}/movie/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getTagsByUserIdAndMovieId(@PathVariable long movieId, @PathVariable long userId) {
        return service.getTagsByMovieAndUser(movieId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void insertOne(@RequestBody InsertTagCommand dto) {
        Tag tag = new Tag(dto.getMovieId(), dto.getUserId(), dto.getTagName(), dto.getTagTime());
        service.insert(tag);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertMany(@RequestBody List<InsertTagCommand> dtos) {
        List<Tag> toInsert = dtos.stream()
                .map(dto -> new Tag(dto.getMovieId(), dto.getUserId(), dto.getTagName(), dto.getTagTime()))
                .collect(toList());
        service.insertMany(toInsert);
    }

    @PostMapping("/user/{userId}/movie/{movieId}/{tagName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertOne(@PathVariable long movieId, @PathVariable long userId, @PathVariable String tagName) {
        Tag rating = new Tag(movieId, userId, tagName, LocalDateTime.now());
        service.insert(rating);
    }

}
