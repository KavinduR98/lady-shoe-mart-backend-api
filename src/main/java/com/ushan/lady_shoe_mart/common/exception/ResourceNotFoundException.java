package com.ushan.lady_shoe_mart.common.exception;

/**
 * Thrown when a requested resource (product, category, etc.) doesn't exist.
 * Maps to HTTP 404.
 */

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
