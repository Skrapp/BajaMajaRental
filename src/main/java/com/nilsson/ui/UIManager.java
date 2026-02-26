package com.nilsson.ui;

import com.nilsson.service.*;

import java.io.BufferedReader;
import java.io.IOException;

public class UIManager {
    private final InputMethod input;

    private final CustomerService customerService;
    private final RentalService rentalService;
    private final BajaMajaService bajaMajaService;
    private final DecorationService decorationService;
    private final PlatformService platformService;

    private boolean running = true;

    public UIManager(BufferedReader reader,
                     CustomerService customerService,
                     RentalService rentalService,
                     BajaMajaService bajaMajaService,
                     DecorationService decorationService,
                     PlatformService platformService) {
        this.input = new InputMethod(reader);
        this.customerService = customerService;
        this.rentalService = rentalService;
        this.bajaMajaService = bajaMajaService;
        this.decorationService = decorationService;
        this.platformService = platformService;
    }

    public void run(UIState startState) throws IOException {
        UIState state = startState;

        while (running) {
            state = nextState(state);
        }
        System.out.println("Tack för idag!");
    }

    private UIState nextState(UIState uiState) throws IOException {
        return switch (uiState) {
            case MAIN_MENU -> new MainMenu(this, input).show();
            case LIST_RENTAL_OBJECTS -> new RentalObjectsList(input, bajaMajaService, platformService, decorationService).show();
            case CREATE_RENTAL_OBJECT -> new RentalObjectCreate(input, bajaMajaService, platformService, decorationService).show();
            case RENT_RENTAL_OBJECT ->
                    new RentalCreate(input, rentalService, customerService, bajaMajaService, platformService, decorationService)
                            .show();
            case END_RENTAL -> new RentalReturn(input, rentalService, customerService).show();
            case LIST_CUSTOMERS -> new CustomerList(input, customerService, rentalService).show();
            case CREATE_NEW_CUSTOMER -> new CustomerCreate(input, customerService).show();
            default -> {
                System.out.println("Denna UIState finns inte: " + uiState.name() + ". Återgår till huvudmeny.");
                yield UIState.MAIN_MENU;
            }
        };
    }

    public void quit() {
        running = false;
    }
}