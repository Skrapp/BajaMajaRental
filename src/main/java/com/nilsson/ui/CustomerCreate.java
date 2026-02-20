package com.nilsson.ui;

import com.nilsson.service.CustomerService;

import java.io.IOException;

public class CustomerCreate {

    private final InputMethod input;
    private final CustomerService customerService;

    public CustomerCreate(InputMethod input, CustomerService customerService) {
        this.input = input;
        this.customerService = customerService;
    }

    public UIState show() throws IOException {
        System.out.println("--------------------");
        System.out.println("Skapa ny kund");
        System.out.println("--------------------");

        String name = input.getInputNotEmpty("Namn:");
        String email = input.getInputNotEmpty("Email:");

        try {
            customerService.createCustomer(name, email);
            System.out.println("Kund skapad.");
        } catch (Exception e) {
            System.out.println("Kunde inte skapa kund: " + e.getMessage());
        }

        return UIState.MAIN_MENU;
    }
}