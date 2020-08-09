package com.poc.movies.batch.tags;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class MainTagsLoaderApp implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MainTagsLoaderApp.class).web(WebApplicationType.NONE).run(args);
    }

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private MainTagsLoaderConfig config;

    @Override
    public void run(String... args) throws Exception {
        if (args == null || args.length != 2) {
            log.error("Invalid arguments");
            log.error("Found:" + Arrays.toString(args));
            log.error("Usage: MainTagsLoaderApp <tags.csv file> <url to post>");
            log.error("Example: MainTagsLoaderApp tags.csv http://localhost:8084");
            throw new IllegalArgumentException("Invalid arguments: " + Arrays.toString(args));
        }

        var filePath = args[0];
        var host = args[1];
        doRun(filePath, host);
    }

    private void doRun(String filePath, String host) throws Exception {
        JobParameters params = new JobParametersBuilder().toJobParameters();
        Job job = config.tagsLoaderJob(filePath, host);
        jobLauncher.run(job, params);
    }
}
