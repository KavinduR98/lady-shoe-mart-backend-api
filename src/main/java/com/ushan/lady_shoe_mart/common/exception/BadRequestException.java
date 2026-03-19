package com.ushan.lady_shoe_mart.common.exception;

/**
 * Thrown for invalid business logic (e.g. duplicate category name).
 * Maps to HTTP 400.
 */

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
