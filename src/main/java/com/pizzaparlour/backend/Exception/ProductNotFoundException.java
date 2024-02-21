package com.pizzaparlour.backend.Exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message){
        super(message);
    }
}
