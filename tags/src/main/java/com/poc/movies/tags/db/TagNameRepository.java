package com.poc.movies.tags.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TagNameRepository extends CrudRepository<TagNameEntity, Long> {

    @Query("SELECT * FROM Tag_Name WHERE tag_name IN (:tagNames)")
    Set<TagNameEntity> findAllByName(Set<String> tagNames);

    @Modifying
    @Query("INSERT IGNORE INTO Tag_Name (tag_name) VALUES (:tagName)")
    int insertIgnore(String tagName);
}
