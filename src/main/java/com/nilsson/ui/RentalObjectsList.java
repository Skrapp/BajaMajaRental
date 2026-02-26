package com.nilsson.ui;

import com.nilsson.entity.rentable.*;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RentalObjectsList {

    private InputMethod input;

    private BajaMajaService bajaMajaService;
    private PlatformService platformService;
    private DecorationService decorationService;

    public RentalObjectsList(InputMethod input, BajaMajaService bajaMajaService, PlatformService platformService, DecorationService decorationService) {
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
                BajaMaja bajaMaja = bajaMajaFilter("uppdatera");
                if(bajaMaja != null) changeBajaMaja(bajaMaja);
            }
            case DECORATION -> {
                decorationService.findAll().forEach(System.out::println);
                Decoration decoration = decorationFilter("uppdatera");
                if(decoration != null) changeDecoration(decoration);
            }case PLATFORM -> {
                platformService.findAll().forEach(System.out::println);
                Platform platform = platformFilter("uppdatera");
                if(platform != null) changePlatform(platform);
            }
        }
        return UIState.MAIN_MENU;
    }

    private BajaMaja bajaMajaFilter(String action) throws IOException {
        do {
            System.out.println("För att " + action + " BajaMaja, skriv in dess id följt av enter");
            System.out.println("""
                För att söka enligt namn skriv "s:" följt av sökord.
                För att filtrera enligt minsta kostnad skriv "min:" följt av pris.
                För att filtrera enligt högsta kostnad skriv "max:" följt av pris.
                För att se endast handikappanpassade skriv "handikapp".
                För att se endast tillgängliga idag skriv "idag".
                
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
                    return bajaMajaService.findById(Long.parseLong(inputChoice));
                } catch (NumberFormatException e) {
                    System.out.println("För att välja, skriv endast ett giltigt id.");
                    continue;
                } catch (RentalObjectNotFoundException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

            // ---- Filtrering ----

            String search = inputChoice.contains("s:")
                    ? input.getSectionFor(inputChoice, "s:")
                    : "";

            double min = inputChoice.contains("min:")
                    ? input.parseDoubleSafe(input.getSectionFor(inputChoice, "min:"))
                    : 0;

            double max = inputChoice.contains("max:")
                    ? input.parseDoubleSafe(input.getSectionFor(inputChoice, "max:"))
                    : 0;

            boolean requireHandicap = inputChoice.contains("handikapp");
            boolean availableToday = inputChoice.contains("idag");

            List<BajaMaja> bajaMajas = bajaMajaService.findFiltered(
                    search,
                    availableToday,
                    LocalDateTime.now(),
                    min,
                    max,
                    requireHandicap
            );

            if (bajaMajas.isEmpty()) {
                System.out.println("Finns inga BajaMajor som matchar sökningen");
                continue;
            }

            bajaMajas.forEach(System.out::println);

        } while (true);
    }


    private Platform platformFilter(String action) throws IOException {
        do {
            System.out.println("För att " + action + " Platform, skriv in dess id följt av enter");
            System.out.println("""
                För att söka enligt namn skriv "s:" följt av sökord.
                För att filtrera enligt minsta kostnad skriv "min:" följt av pris.
                För att filtrera enligt högsta kostnad skriv "max:" följt av pris.
                För att filtrera enligt BajaMaja skriv "b:" följt av BajaMaja-id.
                För att se endast tillgängliga idag skriv "idag".
                
                För att gå tillbaka skriv 0.
                """);

            String inputChoice = input.getInputNotEmpty("Separera anrop med mellanslag");

            if (inputChoice.equals("0")) return null;

            if (Character.isDigit(inputChoice.charAt(0))) {
                try {
                    return platformService.findById(Long.parseLong(inputChoice));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

            String search = inputChoice.contains("s:")
                    ? input.getSectionFor(inputChoice, "s:")
                    : "";

            double min = inputChoice.contains("min:")
                    ? input.parseDoubleSafe(input.getSectionFor(inputChoice, "min:"))
                    : 0;

            double max = inputChoice.contains("max:")
                    ? input.parseDoubleSafe(input.getSectionFor(inputChoice, "max:"))
                    : 0;

            long bajaMajaId = inputChoice.contains("b:")
                    ? input.parseLongSafe(input.getSectionFor(inputChoice, "b:"))
                    : 0;

            boolean availableToday = inputChoice.contains("idag");

            List<Platform> platforms =
                    platformService.findFiltered(search, availableToday, LocalDateTime.now(), min, max, bajaMajaId);

            if(bajaMajaId > 0){
                System.out.println("Plattformar som är kompatibla med: " + bajaMajaService.findById(bajaMajaId).getName());
            }

            if (platforms.isEmpty()) {
                System.out.println("Inga plattformar matchar sökningen");
                continue;
            }

            platforms.forEach(System.out::println);

        } while (true);
    }

    private Decoration decorationFilter(String action) throws IOException {
        do {
            System.out.println("För att " + action + " Decoration, skriv in dess id följt av enter");
            System.out.println("""
                För att söka enligt namn skriv "s:" följt av sökord.
                För att filtrera enligt minsta kostnad skriv "min:" följt av pris.
                För att filtrera enligt högsta kostnad skriv "max:" följt av pris.
                För att filtrera enligt färg skriv "c:" följt av färg.
                För att se endast tillgängliga idag skriv "idag".
                
                För att gå tillbaka skriv 0.
                """);

            String inputChoice = input.getInputNotEmpty("Separera anrop med mellanslag");

            if (inputChoice.equals("0")) return null;

            if (Character.isDigit(inputChoice.charAt(0))) {
                try {
                    return decorationService.findById(Long.parseLong(inputChoice));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

            String search = inputChoice.contains("s:")
                    ? input.getSectionFor(inputChoice, "s:")
                    : "";

            double min = inputChoice.contains("min:")
                    ? input.parseDoubleSafe(input.getSectionFor(inputChoice, "min:"))
                    : 0;

            double max = inputChoice.contains("max:")
                    ? input.parseDoubleSafe(input.getSectionFor(inputChoice, "max:"))
                    : 0;

            List<Color> colors = new ArrayList<>();
            if (inputChoice.contains("c:")) {
                try {
                    Color color = Color.valueOf(
                            input.getSectionFor(inputChoice, "c:").toUpperCase()
                    );
                    colors.add(color);
                } catch (IllegalArgumentException e) {
                    System.out.println("Ogiltig färg, giltiga färger är: ");
                    Arrays.stream(Color.values()).toList().forEach(System.out::println);
                    continue;
                }
            }

            boolean availableToday = inputChoice.contains("idag");

            List<Decoration> decorations =
                    decorationService.findFiltered(search, availableToday, LocalDateTime.now(), min, max, colors);

            if (decorations.isEmpty()) {
                System.out.println("Inga dekorationer matchar sökningen");
                continue;
            }

            decorations.forEach(System.out::println);

        } while (true);
    }


    public void changeBajaMaja(BajaMaja bajaMaja) throws IOException {
        System.out.println("--------------------");
        System.out.println("Ändra BajaMaja");
        System.out.println("--------------------");

        do{
            System.out.println("""
                    För att ändra namn skriv "n:" följt av nytt namn.
                    För att ändra kostnad skriv "p:" följt av nytt pris.
                    För att ändra antal toaletter skriv t: följt av nytt antal
                    För att ändra om den är handikappanpassad skriv h: följt av antingen ja eller nej
                    Endast ett fält kan ändras åt gången
                    
                    För att spara ändringar skriv 0.
                    """);
            String changeinput = input.getInputNotEmpty("Endast ett fält kan ändras åt gången");

            if (changeinput.equals("0")) {
                bajaMajaService.update(bajaMaja);
                System.out.println("Uppdaterad: " + bajaMaja);
                return;
            }

            //Ändring
            if (changeinput.startsWith("n:")) {
                bajaMaja.setName(changeinput.substring(2));
            }else if(changeinput.startsWith("p:")){
                try {
                    double rate = Double.parseDouble(changeinput.substring(2));
                    bajaMaja.setRentalRate(rate);
                } catch (NumberFormatException e) {
                    System.out.println("Går inte sätta ny kostnad då det är fel format");
                }
            }else if(changeinput.startsWith("t:")){
                try {
                    int numOfStalls = Integer.parseInt(changeinput.substring(2));
                    bajaMaja.setNumberOfStalls(numOfStalls);
                } catch (NumberFormatException e) {
                    System.out.println("Går inte sätta ny antal toaletter då det är fel format");
                }
            } else if (changeinput.startsWith("h:")) {
                String yesOrNo = changeinput.substring(2);
               if(yesOrNo.equalsIgnoreCase("ja")){
                   bajaMaja.setHandicap(true);
                   System.out.println("Ändrat till handikappanpassad");
               }else if(yesOrNo.equalsIgnoreCase("nej")){
                   bajaMaja.setHandicap(false);
                   System.out.println("Ändrat till inte handikappanpassad");
               }else System.out.println("Ej ändrad");
            }else System.out.println("Ej ändrad");
        }while (true);
    }

    public void changePlatform(Platform platform) throws IOException {
        System.out.println("--------------------");
        System.out.println("Ändra Platform");
        System.out.println("--------------------");

        do {
            System.out.println("""
                För att ändra namn skriv "n:" följt av nytt namn.
                För att ändra kostnad skriv "p:" följt av nytt pris.
                För att lägga till BajaMaja skriv "b:" följt av BajaMaja-id.
                Endast ett fält kan ändras åt gången.
                
                För att spara ändringar skriv 0.
                """);

            String changeInput = input.getInputNotEmpty("Endast ett fält kan ändras åt gången");

            if (changeInput.equals("0")) {
                platformService.update(platform);
                System.out.println("Uppdaterad: " + platform);
                return;
            }

            // Ändring
            if (changeInput.startsWith("n:")) {
                platform.setName(changeInput.substring(2));

            } else if (changeInput.startsWith("p:")) {
                try {
                    double rate = Double.parseDouble(changeInput.substring(2));
                    platform.setRentalRate(rate);
                } catch (NumberFormatException e) {
                    System.out.println("Går inte sätta ny kostnad då det är fel format");
                }

            } else if (changeInput.startsWith("b:")) {
                try {
                    long bajaMajaId = Long.parseLong(changeInput.substring(2));
                    BajaMaja bajaMaja = bajaMajaService.findById(bajaMajaId);
                    platform.addBajaMaja(bajaMaja);
                    System.out.println("BajaMaja tillagd till plattformen");
                } catch (NumberFormatException e) {
                    System.out.println("Fel format på id");
                } catch (RentalObjectNotFoundException e) {
                    System.out.println(e.getMessage());
                }

            } else {
                System.out.println("Ej ändrad");
            }

        } while (true);
    }

    public void changeDecoration(Decoration decoration) throws IOException {
        System.out.println("--------------------");
        System.out.println("Ändra dekoration");
        System.out.println("--------------------");

        do {
            System.out.println("""
                För att ändra namn skriv "n:" följt av nytt namn.
                För att ändra kostnad skriv "p:" följt av nytt pris.
                För att ändra färg skriv "c:" följt av färg (ex: RED).
                Endast ett fält kan ändras åt gången.
                
                För att spara ändringar skriv 0.
                """);

            String changeinput = input.getInputNotEmpty("Endast ett fält kan ändras åt gången");

            if (changeinput.equals("0")) {
                decorationService.update(decoration);
                System.out.println("Uppdaterad: " + decoration);
                return;
            }

            // Ändra namn
            if (changeinput.startsWith("n:")) {
                decoration.setName(changeinput.substring(2));

                // Ändra pris
            } else if (changeinput.startsWith("p:")) {
                try {
                    double rate = Double.parseDouble(changeinput.substring(2));
                    decoration.setRentalRate(rate);
                } catch (NumberFormatException e) {
                    System.out.println("Går inte sätta ny kostnad då det är fel format");
                }

                // Ändra färg (enum)
            } else if (changeinput.startsWith("c:")) {
                String colorInput = changeinput.substring(2).toUpperCase();

                try {
                    Color newColor = Color.valueOf(colorInput);
                    decoration.setColor(newColor);
                    System.out.println("Färg ändrad till " + newColor);
                } catch (IllegalArgumentException e) {
                    System.out.println("Ogiltig färg. Tillåtna färger är:");
                    for (Color c : Color.values()) {
                        System.out.println("- " + c);
                    }
                }

            } else {
                System.out.println("Ej ändrad");
            }

        } while (true);
    }
}
