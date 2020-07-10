package com.poc.movies.loader.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

@SpringBootApplication
public class MainMovieLoader {

    private static final int THREAD_POOL_SIZE = 20;
    private static final int BATCH_SIZE = 100;

    private final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, InterruptedException {
        new SpringApplicationBuilder(MainMovieLoader.class).web(WebApplicationType.NONE).run(args);
    }

    @Component
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
            String url = args[1];
            doRun(fileName, url);
        }
    }

    private void doRun(String fileName, String baseUrl) throws IOException, InterruptedException {
        Stopwatch sw = Stopwatch.createStarted();
        System.out.println("Reading movies from [" + fileName + "] and POST to [" + baseUrl + "]");

        Path path = Paths.get(fileName);
        List<MovieDescriptor> movieDescriptors = Files.readAllLines(path).stream()
                .skip(1) // First line is header
                .map(this::extractMovie)
                .collect(toList());
        List<String> moviesAsJson = Lists.partition(movieDescriptors, BATCH_SIZE).stream()
                .flatMap(desc -> toJson(desc).stream())
                .collect(toList());
        System.out.println("Found " + moviesAsJson.size() * BATCH_SIZE + " movies to import");

        ExecutorService executors = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        AtomicInteger counter = new AtomicInteger(0);
        List<CompletableFuture<Void>> tasks = moviesAsJson.stream()
                .map(json -> CompletableFuture.supplyAsync(() -> doPost(baseUrl, json), executors))
                .map(future -> future.thenRun(() -> doLogHttpCall(counter, moviesAsJson.size())))
                .collect(toList());

        // Wait until they are all executed
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[]{})).join();

        System.out.println("Done in [" + sw.elapsed(TimeUnit.SECONDS) + "]s");
        System.exit(0);
    }

    /**
     * Extract a movie from a string with format: $movieId,$title,genre1|genre2|...|genreN. <br>
     * Note that $title can contains zero or many coma.
     */
    protected MovieDescriptor extractMovie(String csvLine) {
        String[] split = csvLine.split(",");
        long id = Long.parseLong(split[0]);

        // Extract title
        List<String> titleCells = Arrays.asList(split).subList(1, split.length - 1);
        String title = String.join(",", titleCells);
        if (title.startsWith("\"")) {
            title = title.substring(1, title.length() - 1);
        }

        // Extract genres
        String genresAsStr = split[split.length - 1];
        String[] splitedGenres = genresAsStr.split("\\|");
        Set<String> genres = Set.of(splitedGenres);

        return new MovieDescriptor(id, title, genres);
    }

    protected Optional<String> toJson(List<MovieDescriptor> descs) {
        try {
            return Optional.of(mapper.writeValueAsString(descs));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private String doPost(String baseUrl, String json) {
        try {
            return HttpUtils.post(baseUrl, json);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void doLogHttpCall(AtomicInteger counter, int moviesSize) {
        int count = counter.incrementAndGet();
        System.out.println("POSTed " + count + " / " + moviesSize);
    }

}
