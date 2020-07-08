package com.poc.movies.loader.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.poc.movies.loader.HttpUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainMovieLoader {

    private static final int THREAD_POOL_SIZE = 20;

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args == null || args.length != 2) {
            System.err.println("Invalid arguments");
            System.err.println("Usage: MainMovieLoader <movies.csv file> <url to post>");
            System.exit(1);
        }

        String fileName = args[0];
        String url = args[1];
        new MainMovieLoader().run(fileName, url);
    }

    private final ObjectMapper mapper = new ObjectMapper();

    private void run(String fileName, String baseUrl) throws IOException, InterruptedException {
        Stopwatch sw = Stopwatch.createStarted();
        System.out.println("Reading movies from [" + fileName + "] and POST to [" + baseUrl + "]");

        Path path = Paths.get(fileName);
        List<String> lines = Files.readAllLines(path);
        System.out.println("Found " + (lines.size() - 1) + " movies to import");

        ExecutorService executors = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        AtomicInteger counter = new AtomicInteger(0);
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        // First line is header
        for (int i = 1; i < lines.size(); i++) {
            String csvLine = lines.get(i);
            MovieDescriptor desc = extractMovie(csvLine);
            String json = mapper.writeValueAsString(desc);

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> doPost(baseUrl, json), executors);
            CompletableFuture<Void> task = future.thenRun(() -> doLogHttpCall(counter, lines.size() - 1));
            tasks.add(task);
        }

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

    protected String toJson(MovieDescriptor desc) throws JsonProcessingException {
        return mapper.writeValueAsString(desc);
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
        if (count % 1000 == 0) {
            System.out.println("POSTed " + count + " / " + moviesSize);
        }
    }

}
