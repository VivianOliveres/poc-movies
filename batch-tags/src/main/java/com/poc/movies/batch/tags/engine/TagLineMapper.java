package com.poc.movies.batch.tags.engine;

import com.poc.movies.batch.tags.model.TagsDescriptor;
import org.springframework.batch.item.file.LineMapper;

public class TagLineMapper implements LineMapper<TagsDescriptor> {

    @Override
    public TagsDescriptor mapLine(String line, int lineNumber) throws NumberFormatException {
        //userId,movieId,tag,timestamp
        String[] split = line.split(",");

        long userId = Long.parseLong(split[0]);
        long movieId = Long.parseLong(split[1]);
        String tagName = split[2];
        long timestamp = Long.parseLong(split[3]);

        return new TagsDescriptor(movieId, userId, tagName, timestamp);
    }
}
