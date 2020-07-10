package com.poc.movies.loader.inventory;

import com.google.common.base.Stopwatch;
import com.poc.movies.loader.HttpUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

/**
 * This class will load all movies from movies.csv and compare withe the result of GET /inventory/movie/$id
 */
@SpringBootApplication
public class MainTestMovieLoader {

//    public static void main(String[] args) throws IOException, InterruptedException {
//        new SpringApplicationBuilder(MainTestMovieLoader.class).web(WebApplicationType.NONE).run(args);
//    }
//
//    @Component
    public class CommandLiner implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            if (args == null || args.length != 2) {
                System.err.println("Invalid arguments");
                System.err.println("Found:" + Arrays.toString(args));
                System.err.println("Usage: MainMovieLoader <movies.csv file> <url to post>");
                System.err.println("Example: MainMovieLoader movies.csv http://localhost:8080/inventory/movies");
                System.exit(1);
            }

            String fileName = args[0];
            String host = args[1];
            doRun(fileName, host);
        }
    }

    private void doRun(String fileName, String host) throws IOException {
        Stopwatch sw = Stopwatch.createStarted();
        String baseUrl = host + InventoryUtils.GET_URL;
        System.out.println("Reading movies from [" + fileName + "] and Get from [" + baseUrl + "]");

        Path path = Paths.get(fileName);
        List<MovieDescriptor> movieDescriptors = Files.readAllLines(path).stream()
                .skip(1) // First line is header
                .map(InventoryUtils::extractMovie)
                .collect(toList());

        int i = 1;
        for (MovieDescriptor desc : movieDescriptors) {
            String body = HttpUtils.get(baseUrl + "/" + desc.getId());
            MovieDescriptor returnedMovie = InventoryUtils.MAPPER.readValue(body, MovieDescriptor.class);
            if (!desc.equals(returnedMovie)) {
                System.err.println("Failed to read from/to:\n\t" + desc + "\n\t" + returnedMovie);
            }

            if (i++ % 100 == 0) {
                System.out.println(i + "/" + movieDescriptors.size() + " done...");
            }
        }
        System.out.println("Done in [" + sw.elapsed(TimeUnit.SECONDS) + "]s");
        System.exit(0);
    }

}
