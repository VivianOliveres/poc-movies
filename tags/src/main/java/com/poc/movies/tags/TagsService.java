package com.poc.movies.tags;

import com.google.common.collect.Sets;
import com.poc.movies.tags.db.TagFacadeRepository;
import com.poc.movies.tags.db.TagNameEntity;
import com.poc.movies.tags.db.TagNameRepository;
import com.poc.movies.tags.db.UserTagEntity;
import com.poc.movies.tags.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.stream;

@Slf4j
@Service
public class TagsService {

    private TagFacadeRepository tagRepo;
    private TagNameRepository tagNameRepo;

    public TagsService(TagFacadeRepository tagRepo, TagNameRepository tagNameRepo) {
        this.tagRepo = tagRepo;
        this.tagNameRepo = tagNameRepo;
    }

    public List<Tag> getTagsByMovieId(long movieId) {
        List<UserTagEntity> tagEntities = tagRepo.findAllByMovieId(movieId);
        return userTagEntityToModel(tagEntities);
    }

    private List<Tag> userTagEntityToModel(List<UserTagEntity> tagEntities) {
        Set<Long> tagNameIds = tagEntities.stream().map(UserTagEntity::getTagNameId).collect(toSet());
        Map<Long, String> tagNameById = stream(tagNameRepo.findAllById(tagNameIds).spliterator(), false)
                .collect(Collectors.toMap(TagNameEntity::getTagNameId, TagNameEntity::getTagName));

        return tagEntities.stream()
                .map((ute -> new Tag(ute.getMovieId(), ute.getUserId(), tagNameById.get(ute.getTagNameId()), ute.getTagTime())))
                .collect(toList());
    }

    public List<Tag> getTagsByMovieAndUser(long movieId, long userId) {
        List<UserTagEntity> tagEntities = tagRepo.findAllByMovieIdAndUserId(movieId, userId);
        return userTagEntityToModel(tagEntities);
    }

    public List<Tag> getTagsByUserId(long userId) {
        List<UserTagEntity> tagEntities = tagRepo.findAllByUserId(userId);
        return userTagEntityToModel(tagEntities);
    }

    public void insert(Tag tag) {
        insertMany(List.of(tag));
    }

    public void insertMany(List<Tag> toInsert) {
        // Manage TagName
        Set<String> tagNames = toInsert.stream().map(Tag::getTagName).collect(toSet());
        Set<TagNameEntity> tagNameEntities = tagNameRepo.findAllByName(tagNames);
        if (tagNames.size() != tagNameEntities.size()) {
            // We should create new TagNameEntity
            Set<String> entitiesTagName = tagNameEntities.stream().map(TagNameEntity::getTagName).collect(toSet());
            Set<String> missingTagNames = Sets.difference(tagNames, entitiesTagName);
            missingTagNames.forEach(tagNameRepo::insertIgnore);

            tagNameEntities = tagNameRepo.findAllByName(tagNames);
        }

        Map<String, Long> tagNameById = tagNameEntities.stream().collect(toMap(TagNameEntity::getTagName, TagNameEntity::getTagNameId));
        if (tagNames.size() != tagNameEntities.size()) {
            log.error("Failed to find tags! Expecting {} tags but found {}", tagNames.size(), tagNameEntities.size());
            var toto = tagNames.stream().filter(name -> !tagNameById.containsKey(name)).collect(toList());
            log.error("Missing: {}", toto);
            log.error("tagNameEntities: {}", tagNameEntities);
            log.error("Tags : {}", toInsert.stream().filter(t -> toto.contains(t.getTagName())).collect(toList()));
            log.error("TagNameEntity : {}", tagNameEntities.stream().filter(t -> toto.contains(t.getTagName())).collect(toList()));
            log.error("Missing: {}", toto.size());
            System.exit(0);Diabulus in
        }

        List<UserTagEntity> entitiesToInsert = toInsert.stream()
                .map(tag -> new UserTagEntity(null, tag.getUserId(), tag.getMovieId(), tagNameById.get(tag.getTagName()), tag.getTagTime()))
                .collect(toList());
        tagRepo.insertAll(entitiesToInsert);
    }
}
