package com.poc.movies.inventory.db;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractRepositoryTest {

    @Autowired
    protected MovieRepository movieRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected MovieCategoryRepository movieCategoryRepository;

    @Before
    public void before() {
        movieCategoryRepository.deleteAll();
        movieRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
