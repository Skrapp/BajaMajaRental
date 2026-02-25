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

    public Rental completeReturn(Long rentalId, LocalDateTime returnDate){
        Optional<Rental> rentalOptional = rentalRepo.findById(rentalId);
        if(rentalOptional.isEmpty()) throw new RentalNotFoundException("Finns ingen uthyrning med id: " + rentalId);

        Rental rental = rentalOptional.get();

        //Ser ifall uthyrningen redan är tillbakalämnad
        if(rental.getReturnDate() != null )
            throw new RentalAlreadyReturnedException("Uthyrningen med id " + rental.getId() + " är redan tillbakalämnad.");

        double extra = calculateExtraPayment(rental, returnDate);
        rental.setPrice(rental.getPrice() + extra);

        double refund = calculateRefund(rental, returnDate);
        rental.setPrice(rental.getPrice() - refund);

        rental.setReturnDate(returnDate);
        rentalRepo.save(rental);

        if(extra > 0){
            System.out.println("Payment service happening, kund ska betala: " + extra);
            //Om paymentService, hade kunna gjort med någon typ av try/catch
        }

        if(refund > 0){
            System.out.println("Payment service happening, kund ska bli återbetald: " + refund);
            //Om paymentService, hade kunna gjort med någon typ av try/catch
        }

        return rental;
    }

    private double calculateRefund(Rental rental, LocalDateTime returnDate) {
        //om man returnerar (avbryter) 7 dagar innan startdatum blir allt återbetalat
        if(returnDate.isBefore(rental.getStartDate()) && getDaysBetween(returnDate, rental.getStartDate()) <= 7){
            return rental.getPrice();
        }

        return 0;

    }

    private double calculateExtraPayment(Rental rental, LocalDateTime returnDate) {
        //om man returnerar innan utsatt tid betalar kunden inget extra
        if(returnDate.isBefore(rental.getEndDate())){
            return 0;
        }

        long plannedDays = getDaysBetween(rental.getStartDate(), rental.getEndDate());
        long extraDays = getDaysBetween(rental.getEndDate(), returnDate);
        double dailyRate = rental.getPrice() / plannedDays;
        double lateMultiplier = 1.2;

        return extraDays * dailyRate * lateMultiplier;
    }

    public List<Rental> findAllByCustomerId(Long customerId){
        if(customerId == null || customerId <= 0) throw new IllegalArgumentException("KundID är inte godkänt.");

        return rentalRepo.findAllByCustomerId(customerId);
    }

    public List<Rental> findAllByRentalObjectId(RentalObject rentalObjectType, Long rentalObjectId, boolean fromToday){
        if(rentalObjectId == null || rentalObjectId <= 0) throw new IllegalArgumentException("UthyrningsObjektID är inte godkänt.");

        return rentalRepo.findAllByRentalObjectId(rentalObjectType, rentalObjectId, fromToday);
    }

    private long getDaysBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

}
