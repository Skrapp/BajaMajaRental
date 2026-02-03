package com.nilsson.repo;

import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepo {
    void save(Rental rental);

    List<Rental> findAllByCustomerId(Long customerId);

    boolean availableByRentalObjectAndDate(RentalObject rentalObjectType, long rentalObjectId, LocalDateTime startDate, LocalDateTime endTime);
}
