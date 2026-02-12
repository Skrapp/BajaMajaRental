package com.nilsson.service;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalAlreadyReturnedException;
import com.nilsson.exception.RentalNotFoundException;
import com.nilsson.exception.RentalObjectNotAvailableException;
import com.nilsson.exception.RentalReturnCheckException;
import com.nilsson.repo.RentalRepo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        Double price = getDaysBetween(startDate, endDate) * dailyRate;

        Rental rental = new Rental(customer, rentalObjectType, rentalObjectId, startDate, endDate, price);

        rentalRepo.save(rental);
        return rental;
    }

    public Rental rentalReturnCheck(Long rentalId, LocalDateTime returnDate){
        Optional<Rental> rentalOptional = rentalRepo.findById(rentalId);
        if(rentalOptional.isEmpty()) throw new RentalNotFoundException("Finns ingen rental med det id: " + rentalId);

        Rental rental = rentalOptional.get();
        if(rental.getReturnDate() != null)
            throw new RentalAlreadyReturnedException("Uthyrningen med id " + rental.getId() + " är redan tillbakalämnad.");
        if(rental.isReturned()) return rental;

        if(returnDate.isAfter(rental.getEndDate())){
            double extraPayment = (rental.getPrice() / getDaysBetween(rental.getStartDate(), rental.getEndDate())) * 1.1
                            * getDaysBetween(rental.getEndDate(), returnDate);
            System.out.println("Kräver mer i betalning: " + extraPayment + "kr");
            //Kalla på paymentService?
            rental.setPrice(rental.getPrice() + extraPayment);
        } else if (returnDate.isBefore(rental.getStartDate()) && getDaysBetween(returnDate, rental.getStartDate()) >= 5) {
            System.out.println("Återbetalning: " + rental.getPrice() + "kr");
            //Kalla på paymentService?
            rental.setPrice(0.0);
        }
        rental.setReturned(true);
        rentalRepo.save(rental);
        return rental;
    }

    public Rental returnRental(Long rentalId, LocalDateTime returnDate){
        Optional<Rental> rentalOptional = rentalRepo.findById(rentalId);
        if(rentalOptional.isEmpty()) throw new RentalNotFoundException("Finns ingen rental med det id: " + rentalId);

        Rental rental = rentalOptional.get();
        if(rental.getReturnDate() != null)
            throw new RentalAlreadyReturnedException("Uthyrningen med id " + rental.getId() + " är redan tillbakalämnad.");
        if(!rental.isReturned())
            throw new RentalReturnCheckException("Uthyrningen med id " + rental.getId() + " behöver genomföra en återlämningscheck.");

        rental.setReturnDate(returnDate);
        rentalRepo.save(rental);
        return rental;
    }

    public List<Rental> findAllByCustomerId(Long customerId){
        if(customerId == null || customerId <= 0){
            throw new IllegalArgumentException("KundID krävs.");
            }

        return rentalRepo.findAllByCustomerId(customerId);
    }

    private long getDaysBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

}
