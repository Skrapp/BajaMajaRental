package com.nilsson.repo;

import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepo {
    void save(Rental rental);

    List<Rental> findAllByCustomerId(Long customerId);

    Optional<Rental> findById(Long Id);

    boolean availableByRentalObjectAndDate(RentalObject rentalObjectType, long rentalObjectId, LocalDateTime startDate, LocalDateTime endTime);
}
