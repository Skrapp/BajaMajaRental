package com.nilsson.repo;

import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepo {
    void save(Rental rental);

    Optional<Rental> findById(long Id);

    boolean availableByRentalObjectAndDate(RentalObject rentalObjectType, long rentalObjectId, LocalDateTime startDate, LocalDateTime endTime);

    List<Rental> findAllRentalsByCustomerId(long customerId);

    List<Rental> findActiveRentalsByCustomerId(long customerId);

    List<Rental> findFutureRentalsByCustomerId(long customerId);

    List<Rental> findLateRentalsByCustomerId(long customerId);

    List<Rental> findReturnedRentalsByCustomerId(long customerId);

    List<Rental> findCanceledRentalsByCustomerId(long customerId);

    List<Rental> findNotReturnedRentalsByCustomerId(long customerId);

    List<Rental> findFutureRentalsByRentalObjectId(RentalObject rentalObjectType, long rentalObjectId);
}
