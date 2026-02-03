package com.nilsson.service;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotAvailableException;
import com.nilsson.repo.RentalRepo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalService {

    private final RentalRepo rentalRepo;

    public RentalService(RentalRepo rentalRepo) {
        this.rentalRepo = rentalRepo;
    }

    public Rental createRental(
            Customer customer,
            RentalObject rentalObjectType,
            Long rentalObjectId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Double dailyRate){
        if(customer == null)
            throw new IllegalArgumentException("Det krävs en kund");
        if(rentalObjectType == null)
            throw new IllegalArgumentException("Det krävs ett typ av objekt");
        if(rentalObjectId == null || rentalObjectId < 1)
            throw new IllegalArgumentException("Uthyrnings objektets ID är inte godkänt: " + rentalObjectId);
        if(startDate.isAfter(endDate))
            throw new IllegalArgumentException("Startdatum kan inte vara efter slutdatum");
        if(dailyRate == null || dailyRate < 0)
            throw new IllegalArgumentException("DailyRate är inte godkänt: " + dailyRate);
        if(!rentalRepo.availableByRentalObjectAndDate(rentalObjectType, rentalObjectId, startDate, endDate))
            throw new RentalObjectNotAvailableException(rentalObjectType + " med id  " + rentalObjectId + " är inte tillgängligt de datumen.");

        Double price = getDays(startDate, endDate) * dailyRate;

        Rental rental = new Rental(customer, rentalObjectType, rentalObjectId, startDate, endDate, price);

        rentalRepo.save(rental);
        return rental;
    }

    private long getDays(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }



}
