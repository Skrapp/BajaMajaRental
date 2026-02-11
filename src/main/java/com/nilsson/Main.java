package com.nilsson;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Color;
import com.nilsson.entity.rentable.Decoration;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotAvailableException;
import com.nilsson.repo.*;
import com.nilsson.service.CustomerService;
import com.nilsson.service.BajaMajaService;
import com.nilsson.service.DecorationService;
import com.nilsson.service.RentalService;
import com.nilsson.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Repos
        CustomerRepo customerRepo = new CustomerRepoImpl(sessionFactory);
        BajaMajaRepo bajaMajaRepo = new BajaMajaRepoImpl(sessionFactory);
        RentalRepo rentalRepo = new RentalRepoImpl(sessionFactory);
        DecorationRepo decorationRepo = new DecorationRepoImpl(sessionFactory);

        // Services
        CustomerService customerService = new CustomerService(customerRepo);
        BajaMajaService bajaMajaService = new BajaMajaService(bajaMajaRepo);
        RentalService rentalService = new RentalService(rentalRepo);
        DecorationService decorationService = new DecorationService(decorationRepo);

        Scanner scanner = new Scanner(System.in);

        try {
            // ---------------------------
            // 1) Skapa kund
            // ---------------------------
            /*System.out.println("\n--- Skapa kund ---");
            System.out.print("Namn: ");
            String name = scanner.nextLine();

            System.out.print("E-post: ");
            String email = scanner.nextLine();

            Customer c = customerService.createCustomer(name, email);

            System.out.println("✅ Kund sparad: " + c.getName() + " (" + c.getEmail() + ")");
            System.out.println("   Kund-ID: " + c.getId());

            scanner.nextLine();*/

            //-----------------------------
            // Skapa ny BajaMaja
            //-----------------------------

            /*System.out.println("Skapa en ny BajaMaja");

            System.out.print("namn: ");
            String nameB = scanner.nextLine();

            System.out.print("pris: ");
            int rentalFee = Integer.parseInt(scanner.nextLine());

            System.out.print("Antal toaletter: ");
            int stalls = Integer.parseInt(scanner.nextLine());

            BajaMaja b = bajaMajaService.createBajaMaja(nameB, rentalFee, stalls);

            System.out.println("Sparad: " + b);*/

            //----------------------------
            // Hämta BajaMaja från databas
            //----------------------------

           /* System.out.println("Hämta BajaMaja");

            BajaMaja bajaMaja1 = bajaMajaService.findById(2L).get();

            System.out.println("hittade: " + bajaMaja1);*/

            //-------------------------
            // Hämta alla BajaMajor
            //--------------------------

            /*System.out.println("Hämta alla BajaMajor");

            for(BajaMaja bajaMaja2 : bajaMajaService.findAll()){
                System.out.println(bajaMaja2);
            }*/

            //--------------------------
            // Ändra Bajamaja
            //--------------------------

            /*System.out.println("Välj ett id");

            Long bajaMajaId = Long.parseLong(scanner.nextLine());

            BajaMaja bajaMajaToChange = bajaMajaService.findById(bajaMajaId);
            System.out.println("hämtat");

            bajaMajaToChange.setRentalRate(600);

            bajaMajaService.update(bajaMajaToChange);

            System.out.println("Uppdaterade BajaMaja med id " + bajaMajaToChange.getId());
            */

            //---------------------------
            // Lägg till rental
            //---------------------------

            //Välj kund
            customerService.findAll().forEach(System.out::println);

            System.out.println("Välj kund utifrån id");

            Long customerId = scanner.nextLong();

            Customer customer = customerService.findById(customerId);

            System.out.println("Vald kund: " + customer.getId() + " : " + customer.getName());

            //Välj typ av rentalObject

            System.out.println("Välj kategori:");
            for (RentalObject type : RentalObject.values()) {
                System.out.println(type);
            }

            RentalObject rentalObject = RentalObject.valueOf(scanner.next().trim().toUpperCase());

            switch (rentalObject){
                case BAJAMAJA -> bajaMajaService.findAll().forEach(System.out::println);
                case PLATFORM -> System.out.println("plattformar");
                case DECORATION -> decorationService.findAll().forEach(System.out::println);
                case null, default -> System.out.println("FEL!");
            }

            System.out.println("Välj " + rentalObject.name().toLowerCase() +" utifrån id");

            Long objectId = scanner.nextLong();

            double rentalRate=0;
            switch (rentalObject){
                case BAJAMAJA -> {
                    BajaMaja bajaMaja = bajaMajaService.findById(objectId);
                    System.out.println("Valt objekt: " + bajaMaja.getType() + " " + bajaMaja.getId() + " : " + bajaMaja.getName());
                    rentalRate = bajaMaja.getRentalRate();
                }
                case PLATFORM -> System.out.println("plattformar");
                case DECORATION -> {
                    Decoration decoration = decorationService.findById(objectId);
                    System.out.println("Valt objekt: " + decoration.getType() + " " + decoration.getId() + " : " + decoration.getName());
                    rentalRate = decoration.getRentalRate();
                }
                case null, default -> System.out.println("FEL!");
            }

            //skapa Rental

            try {
                System.out.println("Skapar rental");

                Rental rental = rentalService.createRental(
                        customer,
                        rentalObject,
                        objectId,
                        LocalDateTime.of(2026,2,10,10,59),
                        LocalDateTime.of(2026,2,20,23,59),
                        rentalRate
                );

                System.out.println("Skapade: " + rental);
            } catch (RentalObjectNotAvailableException e) {
                System.out.println("Rental kunde inte skapas: " + e.getMessage());
            }

            //--------------------------
            // Filtrera och sök BajaMaja
            //--------------------------

            bajaMajaService.findAllFiltered("", true,0,0,false).forEach(System.out::println);

            //--------------------------
            // Filtrera och sök Kunder
            //--------------------------

            //customerService.findAllFiltered("sa",true).forEach(System.out::println);

            //-------------------------
            // Visa rentals
            //-------------------------

            /*System.out.println("Visar alla rentals av kund: " + customerService.findById(1L));

            rentalService.findAllByCustomerId(1L).forEach(System.out::println);*/

            //--------------------------
            // Lägg till, visa alla och filtrera dekorationer
            //--------------------------

            /*System.out.println("Skapa en ny dekoration");

            System.out.print("namn: ");
            String nameD = scanner.nextLine();

            System.out.print("pris: ");
            int rentalFee = Integer.parseInt(scanner.nextLine());

            System.out.print("Färg på dekoration: ");
            for(int i = 0 ; i < Color.values().length ; i++){
                System.out.println(i + ": " + Color.values()[i]);
            }
            String colorChoice = scanner.nextLine();
            Color color = Color.valueOf(colorChoice.toUpperCase().trim());

            Decoration b = decorationService.createDecoration(nameD, null, rentalFee, color);

            System.out.println("Sparad: " + b);

             */
            System.out.println("Alla artiklar");
            decorationService.findAll().forEach(System.out::println);

            System.out.println("\nFiltrerade artiklar");
            decorationService.findAllFiltered("", true, 0, 0, null).forEach(System.out::println);



        } finally {
            HibernateUtil.shutdown();
        }
    }
}
