package com.poc.movies.batch.inventory.engine;

import com.poc.movies.batch.inventory.model.MovieDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemListenerSupport;

import java.util.List;

@Slf4j
public class ItemFailureLoggerListener extends ItemListenerSupport<MovieDescriptor, MovieDescriptor> {

    @Override
    public void onReadError(Exception ex) {
        log.error("Encountered error on read", ex);
    }

    @Override
    public void onWriteError(Exception ex, List<? extends MovieDescriptor> items) {
        log.error("Encountered error on write", ex);
    }
}