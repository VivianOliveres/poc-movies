package com.poc.movies.inventory.exceptions;

public class InsertException extends IllegalArgumentException {
    public InsertException(String s) {
        super(s);
    }

    public InsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
