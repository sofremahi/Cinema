package com.tiddev.cinema.service.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {
    private String message;
    private String path;

    private String exception;
    public ErrorDetails(String message, String path) {
        this.message = message;
        this.path = path;
    }

    public ErrorDetails(String message) {
        this.message = message;
    }
}
