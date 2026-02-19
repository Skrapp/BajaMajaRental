package com.nilsson.ui;

import com.nilsson.service.*;

import java.io.BufferedReader;
import java.io.IOException;

public class UIManager {
    private InputMethod input;
    private final CustomerService customerService;
    private final RentalService rentalService;
    private final BajaMajaService bajaMajaService;
    private final DecorationService decorationService;
    private final PlatformService platformService;

    public UIManager(BufferedReader reader,
                     CustomerService customerService,
                     RentalService rentalService,
                     BajaMajaService bajaMajaService,
                     DecorationService decorationService,
                     PlatformService platformService) {
        input = new InputMethod(reader);
        this.customerService = customerService;
        this.rentalService = rentalService;
        this.bajaMajaService = bajaMajaService;
        this.decorationService = decorationService;
        this.platformService = platformService;
    }

    public void show(UIState uiState) throws IOException {
        boolean active = true;
        while (active) {
            switch (uiState) {
                case MAIN_MENU -> uiState = new MainMenu(this, input).show();
                case RENT_RENTAL_OBJECT -> uiState = new RentalCreate(this, input, rentalService, customerService, bajaMajaService, platformService, decorationService).createRental();
                case END_RENTAL -> uiState = new RentalReturn(input, rentalService, customerService).show();
                case LIST_RENTAL_OBJECTS -> uiState = new
                case QUIT -> active = false;
                default -> throw new IllegalArgumentException("Denna UIState finns inte: " + uiState.name());
            }
        }
        System.out.println("Tack för idag!");
    }

    public void quit() {

    }
}
