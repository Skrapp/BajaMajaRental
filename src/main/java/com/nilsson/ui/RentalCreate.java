package com.nilsson.ui;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Decoration;
import com.nilsson.entity.rentable.Platform;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.CustomerNotFoundException;
import com.nilsson.exception.RentalObjectNotAvailableException;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.service.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class RentalCreate {
    private InputMethod input;

    private RentalService rentalService;
    private CustomerService customerService;
    private BajaMajaService bajaMajaService;
    private PlatformService platformService;
    private DecorationService decorationService;

    public RentalCreate(InputMethod input,
                        RentalService rentalService,
                        CustomerService customerService,
                        BajaMajaService bajaMajaService,
                        PlatformService platformService,
                        DecorationService decorationService) {
        this.input = input;
        this.rentalService = rentalService;
        this.customerService = customerService;
        this.bajaMajaService = bajaMajaService;
        this.platformService = platformService;
        this.decorationService = decorationService;
    }

    public UIState show() throws IOException {
        //Välj kund
        customerService.findAll().forEach(System.out::println);

        Customer customer = null;
        while (true) {
            try {
                Long customerId = input.getInputLong("Välj kund utifrån id (skriv 0 för att avbryta).");

                if (customerId == 0L) {
                    System.out.println("Avbryt skapande av uthyrning.");
                    return UIState.MAIN_MENU;
                }
                customer = customerService.findById(customerId);
                break;
            } catch (CustomerNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Vald kund: " + customer.getId() + " : " + customer.getName());

        //Välj typ av rentalObjectType
        System.out.println("Kategorier:");
        for (RentalObject type : RentalObject.values()) {
            System.out.println(type);
        }

        RentalObject rentalObjectType;
        while (true) {
            String rentalCategory = input.getInputNotEmpty("Välj kategori, skriv hela namnet").toUpperCase();
            try {
                rentalObjectType = RentalObject.valueOf(rentalCategory);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ogiltig kategori. Försök igen.");
            }
        }

        switch (rentalObjectType) {
            case BAJAMAJA -> bajaMajaService.findAll().forEach(System.out::println);
            case PLATFORM -> platformService.findAll().forEach(System.out::println);
            case DECORATION -> decorationService.findAll().forEach(System.out::println);
        }

        Long objectId = null;
        double dailyRate = 0;
        boolean noRentalObject = true;
        while (noRentalObject) {
            try {
                objectId = input.getInputLong("Välj id (skriv 0 för att avbryta).");
                if (objectId == 0L) {
                    System.out.println("Avbryt skapande av uthyrning.");
                    return UIState.MAIN_MENU;
                }

                switch (rentalObjectType) {
                    case BAJAMAJA -> {
                        BajaMaja bajaMaja = bajaMajaService.findById(objectId);
                        System.out.println("Valt objekt: " + bajaMaja.getType() + " " + bajaMaja.getId() + " : " + bajaMaja.getName());
                        dailyRate = bajaMaja.getRentalRate();
                        noRentalObject = false;
                    }
                    case PLATFORM -> {
                        Platform platform = platformService.findById(objectId);
                        System.out.println("Valt objekt: " + platform.getType() + " " + platform.getId() + " : " + platform.getName());
                        dailyRate = platform.getRentalRate();
                        noRentalObject = false;
                    }
                    case DECORATION -> {
                        Decoration decoration = decorationService.findById(objectId);
                        System.out.println("Valt objekt: " + decoration.getType() + " " + decoration.getId() + " : " + decoration.getName());
                        dailyRate = decoration.getRentalRate();
                        noRentalObject = false;
                    }
                }
            } catch (RentalObjectNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Alla framtida uthyrningar:");
        rentalService.findFutureRentalsByRentalObjectId(rentalObjectType, objectId).forEach(System.out::println);

        boolean chooseDate = true;
        do{
            //Välj datum
            System.out.println("Välj startdatum.");
            LocalDateTime startDate = input.getInputDate();
            System.out.println("Startdatum: " + startDate);

            System.out.println("Välj slutdatum.");
            LocalDateTime endDate = input.getInputDate();
            System.out.println("Slutdatum: " + endDate);

            //Skapa Rental
            try {
                System.out.println("Skapar rental");

                Rental rental = rentalService.createRental(
                        customer,
                        rentalObjectType,
                        objectId,
                        startDate,
                        endDate,
                        dailyRate
                );

                System.out.println("Skapade: " + rental);
                chooseDate = false;
            } catch (RentalObjectNotAvailableException e) {
                System.out.println("Rental kunde inte skapas: " + e.getMessage());
                if(!input.getInputYesOrNo("Välj annat datum"))
                    chooseDate = false;
            }
        }while (chooseDate);
        return UIState.MAIN_MENU;
    }

}
