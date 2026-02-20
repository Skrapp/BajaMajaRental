package com.nilsson.ui;

import java.io.IOException;

public class MainMenu {
    private UIManager uiManager;
    private InputMethod input;

    private String divider = "---------------------";

    public MainMenu(UIManager uiManager, InputMethod input) {
        this.uiManager = uiManager;
        this.input = input;
    }

    public UIState show() throws IOException {
        System.out.println(divider);
        System.out.println("Välkommen till BajaMaja Rental - För alla dina nödiga behov");
        System.out.println(divider);

        System.out.println("""
                Välj från menyn:
                [1] Lista/uppdatera uthyrningsobjekt
                [2] Skapa nytt uthyrningsobjekt
                [3] Boka uthyrning
                [4] Lämna tillbaka uthyrning
                [5] Lista/uppdatera kunder
                [6] Skapa ny kund
                [0] Avsluta programmet
                """);

        int choice = input.getInputIntMenu("Välj från menyn", 0, 6);

        return switch (choice) {
            case 1 -> UIState.LIST_RENTAL_OBJECTS;
            case 2 -> UIState.CREATE_RENTAL_OBJECT;
            case 3 -> UIState.RENT_RENTAL_OBJECT;
            case 4 -> UIState.END_RENTAL;
            case 5 -> UIState.LIST_CUSTOMERS;
            case 6 -> UIState.CREATE_NEW_CUSTOMER;
            case 0 -> {
                uiManager.quit();
                yield UIState.MAIN_MENU;
            }
            default -> UIState.MAIN_MENU;
        };
    }
}