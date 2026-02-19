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
        while (true) {
            System.out.println("""
                    Välj från menyn:
                    [1] Lista uthyrningsobjekt
                    [2] Skapa nytt uthyrningsobjekt
                    [3] Uppdatera uthyrningsobjekt
                    [4] Boka uthyrning
                    [5] Lämna tillbaka uthyrning
                    [6] Lista kunder
                    [7] Skapa ny kund
                    [8] Uppdatera befintlig kund
                    [0] Avsluta programmet
                    """);
            int choice = input.getInputIntMenu("Välj från menyn", 0, 8);

            switch (choice) {
                case 1 -> {
                    return UIState.LIST_RENTAL_OBJECTS;
                }
                case 2 -> {
                    return UIState.CREATE_RENTAL_OBJECT;
                }
                case 3 -> {
                    return UIState.UPDATE_RENTAL_OBJECT;
                }
                case 4 -> {
                    return UIState.RENT_RENTAL_OBJECT;
                }
                case 5 -> {
                    return UIState.END_RENTAL;
                }
                case 6 -> {
                    return UIState.LIST_CUSTOMERS;
                }
                case 7 -> {
                    return UIState.CREATE_NEW_CUSTOMER;
                }
                case 8 -> {
                    return UIState.UPDATE_CUSTOMER;
                }
                case 0 -> {
                    return UIState.QUIT;
                }
                default -> System.out.println("Något gick fel, försök igen.");
            }
        }
    }
}
