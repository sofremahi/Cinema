package com.tiddev.cinema.service.common.exception;

public class InsufficientFounds extends RuntimeException{
    public InsufficientFounds(String message) {
        super(message);
    }
}
