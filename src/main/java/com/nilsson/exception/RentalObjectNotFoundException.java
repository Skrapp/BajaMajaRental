package com.nilsson.exception;

import com.nilsson.entity.rentable.RentalObject;

public class RentalObjectNotFoundException extends RuntimeException {
    public RentalObjectNotFoundException(RentalObject type, Long id) {
        super("Hittade inte id " + id + " bland typen " + type);
    }

    public RentalObjectNotFoundException(String messege){
        super(messege);
    }
}
