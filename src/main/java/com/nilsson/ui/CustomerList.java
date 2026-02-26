package com.nilsson.ui;

import com.nilsson.entity.Customer;
import com.nilsson.exception.CustomerNotFoundException;
import com.nilsson.service.CustomerService;
import com.nilsson.service.RentalService;

import java.io.IOException;
import java.util.List;

public class CustomerList {

    private final InputMethod input;
    private final CustomerService customerService;
    private final RentalService rentalService;

    public CustomerList(InputMethod input, CustomerService customerService, RentalService rentalService) {
        this.input = input;
        this.customerService = customerService;
        this.rentalService = rentalService;
    }

    public UIState show() throws IOException {

        customerService.findAll().forEach(System.out::println);

        Customer customer = customerFilter("uppdatera");

        if (customer != null) {
            changeCustomer(customer);
        }

        return UIState.MAIN_MENU;
    }

    private Customer customerFilter(String action) throws IOException {
        do {
            System.out.println("För att " + action + " kund, skriv in dess id följt av enter");
            System.out.println("""
            För att söka enligt namn eller email skriv "s:" följt av sökord.
            För att visa endast kunder med minst en uthyrning skriv "hyr:" följt av "alla" för att se alla, "aktiv" för att se alla aktiva eller "sen" för att se de med minst en försening".
            För att sortera på ett specifikt sätt skriv "o:" följt av antingen "namn_stigande" eller "namn_fallande".
            
            Du kan separera flera anrop med mellanslag.
            För att gå tillbaka skriv 0.
            """);

            String inputChoice = input.getInputNotEmpty("Separera anrop med mellanslag");

            // Tillbaka
            if (inputChoice.equals("0")) {
                return null;
            }

            // Välj via id
            if (Character.isDigit(inputChoice.charAt(0))) {
                try {
                    return customerService.findById(Long.parseLong(inputChoice));
                } catch (NumberFormatException e) {
                    System.out.println("För att välja, skriv endast ett giltigt id.");
                } catch (CustomerNotFoundException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                continue;
            }

            // ---- Filtrering ----

            String search = inputChoice.contains("s:")
                    ? input.getSectionFor(inputChoice, "s:")
                    : "";

            CustomerService.RequireRental requireRental = CustomerService.RequireRental.NOT_REQUIRED;
            if (inputChoice.contains("hyr:")){
                switch (input.getSectionFor(inputChoice, "hyr:")){
                    case "alla" -> requireRental = CustomerService.RequireRental.ANY_RENTAL;
                    case "aktiv" -> requireRental = CustomerService.RequireRental.ACTIVE_RENTAL;
                    case "sen" -> requireRental = CustomerService.RequireRental.LATE_RENTAL;
                }
            }

            CustomerService.SortOrder orderBy = CustomerService.SortOrder.DEFAULT;
            if(inputChoice.contains("o:")) {
                switch (input.getSectionFor(inputChoice, "o:")){
                    case "namn_stigande" -> orderBy = CustomerService.SortOrder.NAME_ASC;
                    case "namn_fallande" -> orderBy = CustomerService.SortOrder.NAME_DESC;
                }
            }

            List<Customer> customers =
                    customerService.findAllFiltered(search, requireRental, orderBy);

            // ---- Skriv ut  ----
            if (customers.isEmpty()) {
                System.out.println("Inga kunder matchar sökningen");
                continue;
            }

            switch (requireRental) {
                case ANY_RENTAL -> {
                    for (Customer customer : customers) {
                        System.out.println(customer);
                        System.out.println("Alla uthyrningar:");
                        rentalService.findRentalsByCustomerId(customer.getId(), RentalService.RentalStatus.ALL).forEach(System.out::println);
                        System.out.println();
                    }
                }
                case ACTIVE_RENTAL -> {
                    for (Customer customer : customers) {
                        System.out.println(customer);
                        System.out.println("Aktiva uthyrningar:");
                        rentalService.findRentalsByCustomerId(customer.getId(), RentalService.RentalStatus.ACTIVE).forEach(System.out::println);
                        System.out.println();
                    }
                }
                case LATE_RENTAL -> {
                    for (Customer customer : customers) {
                        System.out.println(customer);
                        System.out.println("Försenade uthyrningar:");
                        rentalService.findRentalsByCustomerId(customer.getId(), RentalService.RentalStatus.LATE).forEach(System.out::println);
                        System.out.println();
                    }
                }
                default -> customers.forEach(System.out::println);
            }

        } while (true);
    }

    public void changeCustomer(Customer customer) throws IOException {
        System.out.println("--------------------");
        System.out.println("Ändra Customer");
        System.out.println("--------------------");

        do {
            System.out.println("Ändrar: " + customer);
            System.out.println("""
                För att ändra namn skriv "n:" följt av nytt namn.
                För att ändra email skriv "e:" följt av ny email.
                Endast ett fält kan ändras åt gången.
                
                För att spara ändringar skriv 0.
                """);

            String changeInput = input.getInputNotEmpty("Separera anrop med mellanslag");

            // Spara och avsluta
            if (changeInput.equals("0")) {
                customerService.update(customer);
                System.out.println("Uppdaterad: " + customer);
                return;
            }

            // Ändra namn
            if (changeInput.startsWith("n:")) {

                String newName = changeInput.substring(2).trim();

                if (newName.isBlank()) {
                    System.out.println("Namnet får inte vara tomt.");
                } else {
                    customer.setName(newName);
                    System.out.println("Namn uppdaterat.");
                }

                // Ändra email
            } else if (changeInput.startsWith("e:")) {
                String newEmail = changeInput.substring(2).trim();

                if(newEmail.isBlank()) {
                    System.out.println("Mail får inte vara tomt");
                }else {
                    customer.setEmail(newEmail);
                    System.out.println("Email uppdaterad.");
                }
            } else {
                System.out.println("Ej ändrad.");
            }

        } while (true);
    }
}
