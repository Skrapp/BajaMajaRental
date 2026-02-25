package com.nilsson.service;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalAlreadyReturnedException;
import com.nilsson.exception.RentalNotFoundException;
import com.nilsson.exception.RentalObjectNotAvailableException;
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
        if(rentalObjectId == null)
            throw new IllegalArgumentException("Uthyrnings objektets ID får inte vara null.");
        if(rentalObjectId < 1)
            throw new IllegalArgumentException("Uthyrnings objektets ID är inte godkänt: " + rentalObjectId);
        if(startDate.isAfter(endDate))
            throw new IllegalArgumentException("Startdatum kan inte vara efter slutdatum");
        if(dailyRate == null)
            throw new IllegalArgumentException("DailyRate får inte vara null.");
        if(dailyRate < 0)
            throw new IllegalArgumentException("DailyRate är inte godkänt: " + dailyRate);
        if(!rentalRepo.availableByRentalObjectAndDate(rentalObjectType, rentalObjectId, startDate, endDate))
            throw new RentalObjectNotAvailableException(rentalObjectType + " med id  " + rentalObjectId + " är inte tillgängligt de datumen.");

        Rental rental = new Rental(customer, rentalObjectType, rentalObjectId, startDate, endDate, dailyRate);

        System.out.println("PaymentService: " + calculatePayment(rental));
        //Om paymentService, om det kastar exception går inte rental igenom

        rentalRepo.save(rental);
        return rental;
    }

    public Rental returnRental(Long rentalId, LocalDateTime returnDate){
        Optional<Rental> rentalOptional = rentalRepo.findById(rentalId);
        if(rentalOptional.isEmpty()) throw new RentalNotFoundException("Finns ingen uthyrning med id: " + rentalId);

        Rental rental = rentalOptional.get();

        //Ser ifall uthyrningen redan är tillbakalämnad
        if(rental.getReturnDate() != null)
            throw new RentalAlreadyReturnedException("Uthyrningen med id " + rental.getId() + " är redan tillbakalämnad.");

        double currentPayment = calculatePayment(rental);
        double extra = calculateExtraPayment(rental.getEndDate(), returnDate, rental.getDailyRate());
        double refund = calculateRefund(rental.getStartDate(), returnDate, currentPayment);
        rental.setFinalPayment(currentPayment + extra - refund);

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

    private double calculatePayment(Rental rental){
        return getDaysBetween(rental.getStartDate(), rental.getEndDate()) * rental.getDailyRate();
    }

    private double calculateRefund(LocalDateTime startDate, LocalDateTime returnDate, double currentPayment) {
        //om man returnerar (avbryter) 7 dagar innan startdatum blir allt återbetalat
        if(returnDate.isBefore(startDate) && getDaysBetween(returnDate, startDate) >= 7){
            return currentPayment;
        }
        //Om man har fler policys kan de läggas in här

        return 0;
    }

    private double calculateExtraPayment(LocalDateTime endDate, LocalDateTime returnDate, double dailyRate) {
        //om man returnerar innan utsatt tid betalar kunden inget extra
        if(returnDate.isBefore(endDate)){
            return 0;
        }

        long extraDays = getDaysBetween(endDate, returnDate);
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
