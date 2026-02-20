package com.nilsson.ui;

import com.nilsson.entity.Customer;
import com.nilsson.exception.CustomerNotFoundException;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.service.CustomerService;

import java.io.IOException;
import java.util.List;

public class CustomerList {

    private final InputMethod input;
    private final CustomerService customerService;

    public CustomerList(InputMethod input, CustomerService customerService) {
        this.input = input;
        this.customerService = customerService;
    }

    public UIState show() throws IOException {

        customerService.findAll().forEach(System.out::println);

        Customer customer = customerFilter("uppdatera");

        if (customer != null) {
            System.out.println("Vald kund: " + customer);
        }

        return UIState.MAIN_MENU;
    }

    private Customer customerFilter(String action) throws IOException {
        do {
            System.out.println("För att " + action + " Customer, skriv in dess id följt av enter");
            System.out.println("""
            För att söka enligt namn eller email skriv "s:" följt av sökord.
            För att visa endast kunder med minst en uthyrning skriv "hyr".
            
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
                } catch (CustomerNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                continue;
            }

            // ---- Filtrering ----

            String search = inputChoice.contains("s:")
                    ? input.getSectionFor(inputChoice, "s:")
                    : "";

            boolean requireRental = inputChoice.contains("hyr");

            List<Customer> customers =
                    customerService.findAllFiltered(search, requireRental);

            if (customers.isEmpty()) {
                System.out.println("Inga kunder matchar sökningen");
                continue;
            }

            customers.forEach(System.out::println);

        } while (true);
    }

    public void changeCustomer(Customer customer) throws IOException {
        System.out.println("--------------------");
        System.out.println("Ändra Customer");
        System.out.println("--------------------");

        do {
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

                customer.setEmail(newEmail);
                System.out.println("Email uppdaterad.");

            } else {
                System.out.println("Ej ändrad.");
            }

        } while (true);
    }
}
