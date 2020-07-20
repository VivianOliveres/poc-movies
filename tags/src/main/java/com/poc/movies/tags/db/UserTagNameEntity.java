package com.poc.movies.tags.db;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("User_Tag_Name")
public class UserTagNameEntity {

    @Id
    @Column("user_tags_name_id")
    Long userTagNameId;

    @Column("user_tag_id")
    long userTagId;

    @Column("tag_name_id")
    long tagNameId;
}
