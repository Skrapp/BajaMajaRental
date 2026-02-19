package com.nilsson.ui;

import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.service.*;

import java.io.IOException;

public class ListRentalObjects {
    private InputMethod input;

    private BajaMajaService bajaMajaService;
    private PlatformService platformService;
    private DecorationService decorationService;

    public ListRentalObjects(InputMethod input, BajaMajaService bajaMajaService, PlatformService platformService, DecorationService decorationService) {
        this.input = input;
        this.bajaMajaService = bajaMajaService;
        this.platformService = platformService;
        this.decorationService = decorationService;
    }

    public UIState show() throws IOException {
        //Välj typ av rentalObject
        System.out.println("Kategorier:");
        for (RentalObject type : RentalObject.values()) {
            System.out.println(type);
        }
        RentalObject rentalObject;
        while (true) {
            String rentalCategory = input.getInputNotEmpty("Välj kategori, skriv hela namnet").toUpperCase();
            try {
                rentalObject = RentalObject.valueOf(rentalCategory);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ogiltig kategori. Försök igen.");
            }
        }

        switch (rentalObject){
            case BAJAMAJA -> {
                bajaMajaService.findAll().forEach(System.out::println);
                return bajaMajaFilter();
            }

        }
    }

    private UIState bajaMajaFilter() {
        System.out.println("""
                För att söka enligt namn skriv "s:" följt av det du vill söka efter.
                För att filtrera enligt minsta kostnad skriv "min:" följt av pris.
                För att filtrera enligt högsta kostnad skriv max: följt av pris.
                För att se endast handikappanpassade skriv "handikapp"
                För att se 
                Du kan separera flera anrop med mellanslag.
                
                För att gå tillbaka till huvudmenyn tryck endast 'enter'.
                """);
        return null;
    }
}
