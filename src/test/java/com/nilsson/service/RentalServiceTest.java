package com.nilsson.service;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.Decoration;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.repo.RentalRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RentalServiceTest {
    private RentalRepo rentalRepo;
    private RentalService rentalService;

    @BeforeEach
    void setUp(){
        rentalRepo = mock(RentalRepo.class);
        rentalService = new RentalService(rentalRepo);
    }

    //Renting when it is available
    @Test
    void rentRentalObject_whenDateIsAvailable(){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(3);

        Customer customer = new Customer("Sara", "sara@mail.com");
        setIdViaReflection(customer, 17L);

        when(rentalRepo
                .availableByRentalObjectAndDate(RentalObject.DECORATION, 20L, startTime, endTime))
                .thenReturn(true);

        Rental rental = rentalService
                .createRental(customer, RentalObject.DECORATION, 20L, startTime, endTime, 100.0);

        assertNotNull(rental);

        assertEquals(RentalObject.DECORATION, rental.getRentalObjectType());
        assertEquals(300, rental.getPrice(), "Kostnaden av rental ska vara dailyRate multiplicerat med antal dagar");
        assertNull(rental.getReturnDate());
        assertSame(customer, rental.getCustomer(), "Rental ska vara kopplad till Customer");

    }
    //Renting when it already is rented
    //returnera innan slutdatum
    //returnera innan startdatum
    //returnera efter slutdatum
    //returnera redan återlämnad

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
