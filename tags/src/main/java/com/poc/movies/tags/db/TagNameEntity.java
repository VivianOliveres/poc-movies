package com.poc.movies.tags.db;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("Tag_Name")
public class TagNameEntity {

    @Id
    @Column("tag_name_id")
    Long tagNameId;

    @Column("tag_name")
    String tagName;
}
