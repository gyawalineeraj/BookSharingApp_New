package com.ng.bsa.exception;

public class InvalidPageNumber extends RuntimeException {
    public InvalidPageNumber(String message) {
        super(message);
    }
}
