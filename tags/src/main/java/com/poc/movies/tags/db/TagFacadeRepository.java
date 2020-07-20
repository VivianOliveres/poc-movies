package com.poc.movies.tags.db;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Repository
public class TagFacadeRepository {

    private TagRepository tagRepo;
    private UserTagNameRepository userTagNameRepo;

    public TagFacadeRepository(TagRepository tagRepo, UserTagNameRepository userTagNameRepo) {
        this.tagRepo = tagRepo;
        this.userTagNameRepo = userTagNameRepo;
    }

    public List<UserTagEntity> findAllByMovieId(long movieId) {
        return tagRepo.findAllByMovieId(movieId);
    }

    public List<UserTagEntity> findAllByMovieIdAndUserId(long movieId, long userId) {
        return tagRepo.findAllByMovieIdAndUserId(movieId, userId);
    }

    public List<UserTagEntity> findAllByUserId(long userId) {
        return tagRepo.findAllByUserId(userId);
    }

    @Transactional
    public void insertAll(List<UserTagEntity> entitiesToInsert) {
        Iterable<UserTagEntity> insertedEntities = tagRepo.saveAll(entitiesToInsert);

        List<UserTagNameEntity> entities = StreamSupport.stream(insertedEntities.spliterator(), false)
                .map(ute -> new UserTagNameEntity(null, ute.getUserTagId(), ute.getTagNameId()))
                .collect(toList());
        userTagNameRepo.saveAll(entities);
    }
}
