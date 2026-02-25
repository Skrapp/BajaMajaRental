package com.nilsson.ui;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.exception.CustomerNotFoundException;
import com.nilsson.exception.RentalAlreadyReturnedException;
import com.nilsson.service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class RentalReturn {
    private InputMethod input;

    private RentalService rentalService;
    private CustomerService customerService;

    public RentalReturn(InputMethod input, RentalService rentalService, CustomerService customerService) {
        this.input = input;
        this.rentalService = rentalService;
        this.customerService = customerService;
    }

    public UIState show() throws IOException {
        System.out.println("Lämnar tillbaka uthyrning.");

        Long customerId;
        Customer customer;
        while (true) {
            try {
                customerId = input.getInputLong("Välj kund utifrån id (skriv 0 för att avbryta).");

                if (customerId == 0L) {
                    System.out.println("Avbryt tillbakalämning.");
                    return UIState.MAIN_MENU;
                }
                customer = customerService.findById(customerId);
                break;
            } catch (CustomerNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Rental> rentals = rentalService.findAllByCustomerId(customerId);
        if(rentals.isEmpty()){
            System.out.println("Kunden har inga uthyrningar.");
            return UIState.MAIN_MENU;
        }
        
        System.out.println("Visar alla rentals av kund: " + customer);
        rentals.forEach(System.out::println);

        Long rentalId;
        while (true){
            rentalId = input.getInputLong("Välj rental-ID (skriv 0 för att avbryta).");
            if (rentalId == 0L) {
                System.out.println("Avbryt tillbakalämning.");
                return UIState.MAIN_MENU;
            }
            Long finalRentalId = rentalId;
            boolean found = rentals.stream().anyMatch(rental -> rental.getId().equals(finalRentalId));
            if (found) {
                break;
            } else {
                System.out.println("ID finns inte");
            }
        }

        try {
            Rental rental = rentalService.returnRental(rentalId, LocalDateTime.now());

            System.out.println("Tillbakalämnad");
            System.out.println(rental);
            return UIState.MAIN_MENU;
        } catch (RentalAlreadyReturnedException e) {
            System.out.println(e.getMessage());
            return UIState.MAIN_MENU;
        }
    }
}
