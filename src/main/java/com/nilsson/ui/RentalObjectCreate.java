package com.nilsson.ui;

import com.nilsson.entity.rentable.Color;
import com.nilsson.entity.rentable.Platform;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.service.BajaMajaService;
import com.nilsson.service.PlatformService;
import com.nilsson.service.DecorationService;

import java.io.IOException;

public class RentalObjectCreate {

    private final InputMethod input;

    private final BajaMajaService bajaMajaService;
    private final PlatformService platformService;
    private final DecorationService decorationService;

    public RentalObjectCreate(InputMethod input,
                              BajaMajaService bajaMajaService,
                              PlatformService platformService,
                              DecorationService decorationService) {
        this.input = input;
        this.bajaMajaService = bajaMajaService;
        this.platformService = platformService;
        this.decorationService = decorationService;
    }

    public UIState show() throws IOException {
        System.out.println("------------------");
        System.out.println("Skapa nytt uthyrningsobjekt");
        System.out.println("------------------");
        System.out.println("Kategorier:");

        for (RentalObject type : RentalObject.values()) {
            System.out.println(type);
        }

        RentalObject rentalObject;

        while (true) {
            String category = input
                    .getInputNotEmpty("Välj kategori, skriv hela namnet")
                    .toUpperCase();

            try {
                rentalObject = RentalObject.valueOf(category);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ogiltig kategori. Försök igen.");
            }
        }

        switch (rentalObject) {
            case BAJAMAJA -> createBajaMaja();
            case PLATFORM -> createPlatform();
            case DECORATION -> createDecoration();
        }

        return UIState.MAIN_MENU;
    }

    private void createBajaMaja() throws IOException {

        System.out.println("Skapa BajaMaja");

        String name = input.getInputNotEmpty("Namn:");
        double rentalRate = input.getInputDouble("Kostnad per dag:");
        int numberOfStalls = input.getInputInt("Antal toaletter:");

        bajaMajaService.createBajaMaja(name, rentalRate, numberOfStalls);

        System.out.println("BajaMaja skapad.");
    }

    private void createPlatform() throws IOException {

        System.out.println("Skapa Platform");

        String name = input.getInputNotEmpty("Namn:");
        double rentalRate = input.getInputDouble("Kostnad per dag:");

        Platform platform = platformService.createPlatform(name, "", rentalRate);

        System.out.println("Platform skapad.");

        // Lägg till BajaMajor
        while (true) {
            bajaMajaService.findAll().forEach(System.out::println);

            System.out.println("""
                För att lägga till kompatibel BajaMaja skriv dess id.
                För att avsluta skriv 0.
                """);

            String choice = input.getInputNotEmpty("Val:");

            if (choice.equals("0")) break;

            try {
                long bajaMajaId = Long.parseLong(choice);

                platformService.addBajaMaja(platform.getId(), bajaMajaId);

                System.out.println("BajaMaja tillagd.");

            } catch (NumberFormatException e) {
                System.out.println("Fel format på id.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void createDecoration() throws IOException {

        System.out.println("Skapa Decoration");

        String name = input.getInputNotEmpty("Namn:");
        double rentalRate = input.getInputDouble("Kostnad per dag:");

        Color color;

        while (true) {
            try {
                String colorInput = input
                        .getInputNotEmpty("Färg (ex: RED):")
                        .toUpperCase();

                color = Color.valueOf(colorInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ogiltig färg. Tillåtna färger är:");
                for (Color c : Color.values()) {
                    System.out.println("- " + c);
                }
            }
        }

        decorationService.createDecoration(name, "",rentalRate, color);

        System.out.println("Decoration skapad.");
    }
}
