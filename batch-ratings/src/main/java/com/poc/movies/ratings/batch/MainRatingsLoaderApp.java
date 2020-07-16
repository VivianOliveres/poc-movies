package com.poc.movies.ratings.batch;

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
public class MainRatingsLoaderApp implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MainRatingsLoaderApp.class).web(WebApplicationType.NONE).run(args);
    }

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private RatingsLoaderConfig config;

    @Override
    public void run(String... args) throws Exception {
        if (args == null || args.length != 2) {
            log.error("Invalid arguments");
            log.error("Found:" + Arrays.toString(args));
            log.error("Usage: MainRatingsLoaderApp <ratings.csv file> <url to post>");
            log.error("Example: MainInventoryLoaderApp movies.csv http://localhost:8083");
            throw new IllegalArgumentException("Invalid arguments: " + Arrays.toString(args));
        }

        var filePath = args[0];
        var host = args[1];
        doRun(filePath, host);
    }

    private void doRun(String filePath, String host) throws Exception {
        JobParameters params = new JobParametersBuilder().toJobParameters();
        Job job = config.ratingsLoaderJob(filePath, host);
        jobLauncher.run(job, params);
    }
}
