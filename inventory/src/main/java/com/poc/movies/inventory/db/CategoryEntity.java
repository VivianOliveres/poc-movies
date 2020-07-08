package com.poc.movies.inventory.db;

import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("Categories")
public class CategoryEntity {

    @Id
    @Column("category_id")
    @With
    Long categoryId;

    @Column("category_name")
    String categoryName;
}
