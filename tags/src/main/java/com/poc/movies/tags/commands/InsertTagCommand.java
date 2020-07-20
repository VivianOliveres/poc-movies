package com.poc.movies.tags.commands;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class InsertTagCommand {

    long movieId;

    long userId;

    String tagName;

    LocalDateTime tagTime;

}
