package com.poc.movies.batch.tags;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.batch.tags.engine.TagLineMapper;
import com.poc.movies.batch.tags.engine.TagRestChecker;
import com.poc.movies.batch.tags.engine.TagRestWriter;
import com.poc.movies.batch.tags.model.TagsDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.Objects;

@Slf4j
@Configuration
@EnableBatchProcessing
public class MainTagsLoaderConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ObjectMapper mapper;


    public Job tagsLoaderJob(String filePath, String host) {
        return jobBuilderFactory.get("tagsLoaderJob")
                .start(stepLoad(filePath, host))
                .next(stepCheck(filePath, host))
                .build();
    }

    private Step stepLoad(String filePath, String host) {
        return stepBuilderFactory.get("stepLoad").<TagsDescriptor, TagsDescriptor>chunk(1000)
                .reader(reader(filePath))
                .writer(restWriter(host))
                .taskExecutor(taskExecutor())
                .build();
    }

    private TagRestWriter restWriter(String host) {
        Objects.requireNonNull(host);
        return new TagRestWriter(host, mapper);
    }

    private Step stepCheck(String filePath, String host) {
        return stepBuilderFactory.get("stepCheck").<TagsDescriptor, TagsDescriptor>chunk(1)
                .reader(reader(filePath))
                .writer(restChecker(host))
                .taskExecutor(taskExecutor())
                .build();
    }

    private TagRestChecker restChecker(String host) {
        Objects.requireNonNull(host);
        return new TagRestChecker(host, mapper);
    }

    private FlatFileItemReader<TagsDescriptor> reader(String filePath) {
        Objects.requireNonNull(filePath);
        return new FlatFileItemReaderBuilder<TagsDescriptor>()
                .name("tagsDescriptorReader")
                .resource(new FileSystemResource(filePath))
                .lineMapper(new TagLineMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("tags");
        taskExecutor.setConcurrencyLimit(Runtime.getRuntime().availableProcessors());
        return taskExecutor;
    }
}
