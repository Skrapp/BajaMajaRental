package com.nilsson.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class InputMethod {
    private final BufferedReader reader;

    public InputMethod(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Ser till att det användaren skickar inte är tom, repeterar tills rätt format är inskikckat
     * @param askFromUser vad användaren ska svara på
     * @return en sträng med användarens svar
     * @throws IOException
     */
    public String getInputNotEmpty(String askFromUser) throws IOException{
        while (true) {
            System.out.println(askFromUser + ":");
            String input = reader.readLine().trim();
            if (input.isEmpty()) {
                System.out.println(askFromUser + " kan inte vara tom.");
                continue;
            }
            return input;
        }
    }

    /**
     * Ser till att det användaren skickar inte är tom och är kompatibel som double, repeterar tills rätt format är inskickat
     * @param askFromUser vad användaren ska svara på
     * @return en double av användarens svar
     * @throws IOException
     */
    public double getInputDouble(String askFromUser) throws IOException{
        while (true) {
            String input = getInputNotEmpty(askFromUser);
            double inputAsDouble = 0;
            try {
                inputAsDouble = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Ange ett tal");
                continue;
            }

            return inputAsDouble;
        }
    }

    /**
     * Ser till att det användaren skickar inte är tom och är kompatibel som int, repeterar tills rätt format är inskickat
     * @param askFromUser vad användaren ska svara på
     * @return en int som finns i menyn
     * @throws IOException
     */
    public int getInputInt(String askFromUser) throws IOException {
        while (true){
            System.out.println(askFromUser + ". Svara med ett heltal.");
            try {
                return Integer.parseInt(reader.readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Skriv ett heltal");
            }

        }
    }

    /**
     * Ser till att det användaren skickar inte är tom och är kompatibel som int, repeterar tills rätt format är inskickat
     * @param askFromUser vad användaren ska svara på
     * @return en int som finns i menyn
     * @throws IOException
     */
    public Long getInputLong(String askFromUser) throws IOException {
        while (true){
            System.out.println(askFromUser + ". Svara med ett heltal.");
            try {
                return Long.parseLong(reader.readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Skriv ett heltal");
            }

        }
    }

    /**
     * Används för menyval. Ser till att det användaren skickar inte är tom och är kompatibel som int och inom angivna
     * gränser, repeterar tills rätt format är inskickat
     * @param askFromUser vad användaren ska svara på
     * @param maximum högsta möjliga val
     * @param minimum lägsta möjliga val
     * @return en int som finns i menyn
     * @throws IOException
     */
    public int getInputIntMenu(String askFromUser, int minimum, int maximum) throws IOException {
        while (true){
            System.out.println(askFromUser + ". Svara med tal från menyn.");
            try {
                int input = Integer.parseInt(reader.readLine().trim());
                if (input <= maximum || input >= minimum){
                    return input;
                }else {
                    System.out.println("Skriv ett av talen från menyn.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Skriv ett heltal");
            }
        }
    }

    /**
     * Ser till att det användaren skickar in antingen är ja eller nej och returnerar boolean.
     * @param askFromUser vad användaren ska svara på
     * @return true om användare skriver ja, false om användaren skriver nej
     * @throws IOException
     */
    public boolean getInputYesOrNo(String askFromUser) throws IOException{
        while (true) {
            System.out.println(askFromUser + "? Svara med 'Ja' eller 'Nej'.");
            String input = reader.readLine().trim();
            if (input.equalsIgnoreCase("ja")) {
                return true;
            }else if(input.equalsIgnoreCase("nej")){
                return false;
            } else {
                System.out.println("Svara med antingen 'Ja' eller 'Nej'.");
            }
        }
    }

    public LocalDateTime getInputDate() throws IOException {
        while (true) {
            System.out.println("Skriv datum i formatet yyyy-mm-dd:hh (skriv 0 för att hyra från idag)");
            String input = reader.readLine().trim();
            if(input.equals("0")){
                return LocalDateTime.now();
            }
            try {
                return LocalDateTime.parse(input.replace(':', 'T') + ":00:00");
            } catch (Exception e) {
                System.out.println("Fel format på datumet, försök igen");
            }
        }
    }

    //Separerar strängen så att endast det svar som gäller given prefix skickas tillbaka
    public String getSectionFor(String entireString, String prefix) {
        entireString = entireString.concat(" ");
        return entireString.substring(entireString.indexOf(prefix) + prefix.length(), entireString.indexOf(" ", entireString.indexOf(prefix)));
    }

    public double parseDoubleSafe(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Long parseLongSafe(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
