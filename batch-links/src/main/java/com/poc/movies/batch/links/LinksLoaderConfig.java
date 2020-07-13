package com.poc.movies.batch.links;

import com.poc.movies.batch.links.engine.LinksDescriptorLineMapper;
import com.poc.movies.batch.links.engine.LinksDescriptorRestChecker;
import com.poc.movies.batch.links.engine.LinksDescriptorRestWriter;
import com.poc.movies.batch.links.model.LinksDescriptor;
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
public class LinksLoaderConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    public Job linksLoaderJob(String filePath, String host) {
        return jobBuilderFactory.get("linksLoaderJob")
                .start(stepLoad(filePath, host))
                .next(stepCheck(filePath, host))
                .build();
    }

    private Step stepLoad(String filePath, String host) {
        return stepBuilderFactory.get("stepLoad").<LinksDescriptor, LinksDescriptor>chunk(100)
                .reader(reader(filePath))
                .writer(restWriter(host))
                .taskExecutor(taskExecutor())
                .build();
    }

    private LinksDescriptorRestWriter restWriter(String host) {
        Objects.requireNonNull(host);
        return new LinksDescriptorRestWriter(host);
    }

    private Step stepCheck(String filePath, String host) {
        return stepBuilderFactory.get("stepCheck").<LinksDescriptor, LinksDescriptor>chunk(1)
                .reader(reader(filePath))
                .writer(restChecker(host))
                .taskExecutor(taskExecutor())
                .build();
    }

    private LinksDescriptorRestChecker restChecker(String host) {
        Objects.requireNonNull(host);
        return new LinksDescriptorRestChecker(host);
    }

    private FlatFileItemReader<LinksDescriptor> reader(String filePath) {
        Objects.requireNonNull(filePath);
        return new FlatFileItemReaderBuilder<LinksDescriptor>()
                .name("linksDescriptorReader")
                .resource(new FileSystemResource(filePath))
                .lineMapper(new LinksDescriptorLineMapper())
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
