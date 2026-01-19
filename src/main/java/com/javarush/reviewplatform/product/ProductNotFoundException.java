package com.javarush.reviewplatform.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String s) {
    }

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }
}
