package com.example.demo.__shared.exception;


public class BadRequestException extends RuntimeException {

    public BadRequestException(String massage) {
        super(massage);
    }
}
