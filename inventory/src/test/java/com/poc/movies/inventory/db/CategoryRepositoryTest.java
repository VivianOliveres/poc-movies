package com.poc.movies.inventory.db;

import com.poc.movies.inventory.InventoryApp;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InventoryApp.class})
@Sql(scripts = {"/data-mysql.sql"})
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MovieCategoryRepository movieCategoryRepository;

    private static final String CATEGORY_NAME = "action";

    @Test
    public void should_find_nothing_in_table_at_startup() {
        List<CategoryEntity> entities = Lists.newArrayList(categoryRepository.findAll());
        assertThat(entities).isEmpty();
    }

    @Test
    public void should_insert_category() {
        CategoryEntity insertedEntity = categoryRepository.save(new CategoryEntity(null, CATEGORY_NAME));

        assertThat(insertedEntity).isNotNull();
        assertThat(insertedEntity.getCategoryId()).isNotNull();
        assertThat(insertedEntity.getCategoryName()).isEqualTo(CATEGORY_NAME);

        List<CategoryEntity> entities = Lists.newArrayList(categoryRepository.findAll());
        assertThat(entities).containsExactly(insertedEntity);
    }

    @Test
    public void should_delete_existing_category() {
        CategoryEntity insertedEntity = categoryRepository.save(new CategoryEntity(null, CATEGORY_NAME));
        categoryRepository.delete(insertedEntity);

        List<CategoryEntity> entities = Lists.newArrayList(categoryRepository.findAll());
        assertThat(entities).isEmpty();
    }

}
