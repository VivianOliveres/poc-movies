package com.poc.movies.tags.db;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Value
@Table("User_Tag")
public class UserTagEntity {

    @Id
    @Column("user_tag_id")
    Long userTagId;

    @Column("user_id")
    long userId;

    @Column("movie_id")
    long movieId;

    @Column("tag_name_id")
    long tagNameId;

    @Column("tag_time")
    LocalDateTime tagTime;
}
