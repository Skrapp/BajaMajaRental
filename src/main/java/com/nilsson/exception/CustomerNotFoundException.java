package com.nilsson.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Hittade inte kund med valt id: " + id);
    }

    public CustomerNotFoundException(String message){
        super(message);
    }
}
