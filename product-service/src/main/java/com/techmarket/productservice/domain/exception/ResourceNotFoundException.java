package com.techmarket.productservice.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super("%s not found with identifier: %s".formatted(resourceName, identifier));
    }
}
