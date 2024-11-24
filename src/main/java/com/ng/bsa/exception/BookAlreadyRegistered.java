package com.ng.bsa.exception;

public class BookAlreadyRegistered extends Exception{
    public BookAlreadyRegistered(String message) {
        super(message);
    }
}
