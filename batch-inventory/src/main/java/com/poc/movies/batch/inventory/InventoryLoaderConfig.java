package com.poc.movies.batch.inventory;

import com.poc.movies.batch.inventory.engine.MovieDescriptorLineMapper;
import com.poc.movies.batch.inventory.engine.MovieDescriptorProcessor;
import com.poc.movies.batch.inventory.engine.MovieDescriptorRestChecker;
import com.poc.movies.batch.inventory.engine.MovieDescriptorRestWriter;
import com.poc.movies.batch.inventory.model.MovieDescriptor;
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
public class InventoryLoaderConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    public Job inventoryLoaderJob(String filePath, String host) {
        return jobBuilderFactory.get("inventoryLoaderJob")
                .start(stepLoad(filePath, host))
                .next(stepCheck(filePath, host))
                .build();
    }

    private Step stepLoad(String filePath, String host) {
        return stepBuilderFactory.get("stepLoad").<MovieDescriptor, MovieDescriptor>chunk(100)
                .reader(reader(filePath))
                .processor(new MovieDescriptorProcessor())
                .writer(restWriter(host))
                .taskExecutor(taskExecutor())
                .build();
    }

    private MovieDescriptorRestWriter restWriter(String host) {
        Objects.requireNonNull(host);
        return new MovieDescriptorRestWriter(host);
    }

    private Step stepCheck(String filePath, String host) {
        return stepBuilderFactory.get("stepCheck").<MovieDescriptor, MovieDescriptor>chunk(1)
                .reader(reader(filePath))
                .processor(new MovieDescriptorProcessor())
                .writer(restChecker(host))
                .taskExecutor(taskExecutor())
                .build();
    }

    private MovieDescriptorRestChecker restChecker(String host) {
        Objects.requireNonNull(host);
        return new MovieDescriptorRestChecker(host);
    }

    private FlatFileItemReader<MovieDescriptor> reader(String filePath) {
        Objects.requireNonNull(filePath);
        return new FlatFileItemReaderBuilder<MovieDescriptor>()
                .name("movieDescriptorReader")
                .resource(new FileSystemResource(filePath))
                .lineMapper(new MovieDescriptorLineMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("batch-inventory");
        taskExecutor.setConcurrencyLimit(Runtime.getRuntime().availableProcessors());
        return taskExecutor;
    }
}
