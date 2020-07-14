package com.poc.movies.ratings;

import com.poc.movies.ratings.db.RatingEntity;
import com.poc.movies.ratings.db.RatingRepository;
import com.poc.movies.ratings.model.Rating;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class RatingsService {

    private RatingRepository repository;

    public RatingsService(RatingRepository repository) {
        this.repository = repository;
    }

    public Rating insert(Rating rating) {
        RatingEntity entityToSave = fromModel(rating);
        RatingEntity entitySaved = repository.save(entityToSave);
        return toModel(entitySaved);
    }

    public void insertMany(List<Rating> ratingsToInsert) {
        ratingsToInsert.forEach(this::insert);
    }

    private RatingEntity fromModel(Rating rating) {
        return new RatingEntity(null, rating.getMovieId(), rating.getUserId(), rating.getRatingValue(), rating.getRatingTime());
    }

    private Rating toModel(RatingEntity entity) {
        return new Rating(entity.getMovieId(), entity.getUserId(), entity.getRatingValue(), entity.getRatingTime());
    }

    public Rating getRating(long movieId, long userId) {
        Optional<Rating> maybeRating = repository.findByIds(movieId, userId).map(this::toModel);
        return maybeRating.orElseThrow(() -> new IllegalArgumentException("No ratings for movie[" + movieId + "] and user[" + userId + "]"));
    }

    public List<Rating> getRatingsByUserId(long userId) {
        return repository.findByUserId(userId).stream().map(this::toModel).collect(toList());
    }

    public List<Rating> getRatingsByMovieId(long movieId) {
        return repository.findByMovieId(movieId).stream().map(this::toModel).collect(toList());
    }
}
