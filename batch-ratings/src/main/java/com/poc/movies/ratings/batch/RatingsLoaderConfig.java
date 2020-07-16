package com.poc.movies.ratings.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.movies.ratings.batch.engine.RatingsDescriptorLineMapper;
import com.poc.movies.ratings.batch.engine.RatingsDescriptorRestChecker;
import com.poc.movies.ratings.batch.engine.RatingsDescriptorRestWriter;
import com.poc.movies.ratings.batch.model.RatingsDescriptor;
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
public class RatingsLoaderConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ObjectMapper mapper;


    public Job ratingsLoaderJob(String filePath, String host) {
        return jobBuilderFactory.get("ratingsLoaderJob")
                .start(stepLoad(filePath, host))
                .next(stepCheck(filePath, host))
                .build();
    }

    private Step stepLoad(String filePath, String host) {
        return stepBuilderFactory.get("stepLoad").<RatingsDescriptor, RatingsDescriptor>chunk(1000)
                .reader(reader(filePath))
                .writer(restWriter(host))
                .taskExecutor(taskExecutor())
                .build();
    }

    private RatingsDescriptorRestWriter restWriter(String host) {
        Objects.requireNonNull(host);
        return new RatingsDescriptorRestWriter(host, mapper);
    }

    private Step stepCheck(String filePath, String host) {
        return stepBuilderFactory.get("stepCheck").<RatingsDescriptor, RatingsDescriptor>chunk(1)
                .reader(reader(filePath))
                .writer(restChecker(host))
                .taskExecutor(taskExecutor())
                .build();
    }

    private RatingsDescriptorRestChecker restChecker(String host) {
        Objects.requireNonNull(host);
        return new RatingsDescriptorRestChecker(host, mapper);
    }

    private FlatFileItemReader<RatingsDescriptor> reader(String filePath) {
        Objects.requireNonNull(filePath);
        return new FlatFileItemReaderBuilder<RatingsDescriptor>()
                .name("ratingsDescriptorReader")
                .resource(new FileSystemResource(filePath))
                .lineMapper(new RatingsDescriptorLineMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("ratings");
        taskExecutor.setConcurrencyLimit(Runtime.getRuntime().availableProcessors());
        return taskExecutor;
    }
}
