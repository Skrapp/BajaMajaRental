package com.nilsson.service;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotAvailableException;
import com.nilsson.repo.RentalRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentalServiceTest {
    private RentalRepo rentalRepo;
    private RentalService rentalService;

    @BeforeEach
    void setUp(){
        rentalRepo = mock(RentalRepo.class);
        rentalService = new RentalService(rentalRepo);
    }

    //Hyr ett objekt som är tillgängligt
    @Test
    void rentRentalObject_whenDateIsAvailable_shouldSaveRental_(){
        RentalObject rentalObject = RentalObject.DECORATION;
        long rentalObjectId = 20L;
        double dailyRate = 100;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(3);

        Customer customer = new Customer("Sara", "sara@mail.com");
        setIdViaReflection(customer, 17L);

        when(rentalRepo
                .availableByRentalObjectAndDate(rentalObject, rentalObjectId, startTime, endTime))
                .thenReturn(true);


        Rental rental = rentalService
                .createRental(customer, RentalObject.DECORATION, rentalObjectId, startTime, endTime, dailyRate);

        assertNotNull(rental);

        assertEquals(RentalObject.DECORATION, rental.getRentalObjectType());
        assertEquals(rentalObjectId, rental.getRentalObjectId());
        assertEquals(dailyRate, rental.getDailyRate());
        assertEquals(startTime, rental.getStartDate());
        assertEquals(endTime, rental.getEndDate());
        assertEquals(dailyRate*3, rental.getOriginalPayment());
        assertNull(rental.getReturnDate());
        assertNull(rental.getExtraPayment());
        assertNull(rental.getRefund());
        assertSame(customer, rental.getCustomer(), "Rental ska vara kopplad till Customer");
    }
    //Hyr ett objekt som redan har en bokning under de datumen
    @Test
    void rentRentalObject_whenDateIsNotAvailable_shouldThrowException_andNotSave(){
        RentalObject rentalObject = RentalObject.DECORATION;
        long rentalObjectId = 20L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(3);
        double dailyRate = 100;
        Customer customer = new Customer("Sara", "sara@mail.com");
        setIdViaReflection(customer, 17L);

        when(rentalRepo
                .availableByRentalObjectAndDate(rentalObject, rentalObjectId, startTime, endTime))
                .thenReturn(false);

        assertThrows(RentalObjectNotAvailableException.class,
                () -> rentalService.createRental(customer, rentalObject, rentalObjectId, startTime, endTime, dailyRate));

        verify(rentalRepo).availableByRentalObjectAndDate(rentalObject, rentalObjectId, startTime, endTime);
        verify(rentalRepo, never()).save(any());
    }

    //returnera innan slutdatum
    @Test
    void returnRental_afterStartDateAndBeforeEndDate_shouldChangeReturnDate_ShouldGetNoRefundAndNoExtraPayment(){
        long rentalId = 2L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(2);
        double dailyRate = 100;

        when(rentalRepo
                .availableByRentalObjectAndDate(RentalObject.DECORATION, 20L, startTime, endTime))
                .thenReturn(true);

        Rental rentalPre = rentalService.createRental(
                new Customer("Sara", "sara@mail.com"),  RentalObject.DECORATION, 20L,
                startTime, endTime, dailyRate);
        setIdViaReflection(rentalPre, rentalId);

        when(rentalRepo
                .findById(rentalId))
                .thenReturn(Optional.of(rentalPre));

        Rental rental = rentalService
                .returnRental(rentalId, endTime.minusHours(4));

        assertNotNull(rental);

        assertEquals(endTime.minusHours(4), rental.getReturnDate());
        assertEquals(rentalPre.getOriginalPayment(), rental.getOriginalPayment());
        assertEquals(0, rental.getExtraPayment());
        assertEquals(0, rental.getRefund());
    }
    //returnera innan startdatum
    @Test
    void returnRental_sevenDaysBeforeStartDate_shouldChangeReturnDateAndRefund_ShouldGetNoExtraPayment(){
        long rentalId = 2L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(2);
        double dailyRate = 100;

        when(rentalRepo
                .availableByRentalObjectAndDate(RentalObject.DECORATION, 20L, startTime, endTime))
                .thenReturn(true);

        Rental rentalPre = rentalService.createRental(
                new Customer("Sara", "sara@mail.com"),  RentalObject.DECORATION, 20L,
                startTime, endTime, dailyRate);
        setIdViaReflection(rentalPre, rentalId);

        when(rentalRepo
                .findById(rentalId))
                .thenReturn(Optional.of(rentalPre));

        Rental rental = rentalService
                .returnRental(rentalId, startTime.minusDays(7));

        assertNotNull(rental);

        assertEquals(rentalPre.getOriginalPayment(), rental.getOriginalPayment());
        assertEquals(0, rental.getExtraPayment());
        assertEquals(rentalPre.getOriginalPayment(), rental.getRefund());
    }

    @Test
    void returnRental_lessThanSevenDaysBeforeStartDate_shouldChangeReturnDate_ShouldGetNoRefundAndNoExtraPayment(){
        long rentalId = 2L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(2);
        double dailyRate = 100;

        when(rentalRepo
                .availableByRentalObjectAndDate(RentalObject.DECORATION, 20L, startTime, endTime))
                .thenReturn(true);

        Rental rentalPre = rentalService.createRental(
                new Customer("Sara", "sara@mail.com"),  RentalObject.DECORATION, 20L,
                startTime, endTime, dailyRate);
        setIdViaReflection(rentalPre, rentalId);

        when(rentalRepo
                .findById(rentalId))
                .thenReturn(Optional.of(rentalPre));

        Rental rental = rentalService
                .returnRental(rentalId, startTime.minusDays(6).minusHours(23));

        assertNotNull(rental);

        assertEquals(rentalPre.getOriginalPayment(), rental.getOriginalPayment());
        assertEquals(0, rental.getExtraPayment());
        assertEquals(0, rental.getRefund());
    }


    //returnera efter slutdatum
    //returnera redan återlämnad
    @Test
    void returnRental_ReturningAnHourLateShouldCountAsOneChargeableLateDayFee(){
        long rentalId = 2L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(2);
        double dailyRate = 100;

        when(rentalRepo
                .availableByRentalObjectAndDate(RentalObject.DECORATION, 20L, startTime, endTime))
                .thenReturn(true);

        Rental rentalPre = rentalService.createRental(
                new Customer("Sara", "sara@mail.com"),  RentalObject.DECORATION, 20L,
                startTime, endTime, dailyRate);
        setIdViaReflection(rentalPre, rentalId);

        when(rentalRepo
                .findById(rentalId))
                .thenReturn(Optional.of(rentalPre));

        Rental rental = rentalService
                .returnRental(rentalId, endTime.plusHours(1));

        assertNotNull(rental);

        assertEquals(dailyRate * 2, rental.getOriginalPayment());
        assertEquals(dailyRate * 1.2, rental.getExtraPayment());
        assertEquals(0, rental.getRefund());
    }
    @Test
    void returnRental_ReturningLessThanAnHourLateShouldNotChargeMore(){
        long rentalId = 2L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(2);
        double dailyRate = 100;

        when(rentalRepo
                .availableByRentalObjectAndDate(RentalObject.DECORATION, 20L, startTime, endTime))
                .thenReturn(true);

        Rental rentalPre = rentalService.createRental(
                new Customer("Sara", "sara@mail.com"),  RentalObject.DECORATION, 20L,
                startTime, endTime, dailyRate);
        setIdViaReflection(rentalPre, rentalId);

        when(rentalRepo
                .findById(rentalId))
                .thenReturn(Optional.of(rentalPre));

        Rental rental = rentalService
                .returnRental(rentalId, endTime.plusMinutes(59));

        assertNotNull(rental);

        assertEquals(dailyRate * 2, rental.getOriginalPayment());
        assertEquals(0, rental.getRefund());
        assertEquals(0, rental.getExtraPayment());
    }

    private static void setIdViaReflection(Object entity, Long id) {
        try {
            // Hämtar fältet med namnet "id" även om det är private
            var field = entity.getClass().getDeclaredField("id");

            // Tillåter åtkomst trots att fältet är private
            field.setAccessible(true);

            // Sätter värdet på fältet "id" i objektet
            field.set(entity, id);
        } catch (Exception e) {
            // Om det går fel vill vi faila snabbt och tydligt i testet
            throw new RuntimeException("Kunde inte sätta id via reflection", e);
        }
    }
}
