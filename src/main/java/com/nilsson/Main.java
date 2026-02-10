package com.nilsson;

import com.nilsson.entity.Customer;
import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.repo.*;
import com.nilsson.service.CustomerService;
import com.nilsson.service.BajaMajaService;
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

        // Services
        CustomerService customerService = new CustomerService(customerRepo);
        BajaMajaService bajaMajaService = new BajaMajaService(bajaMajaRepo);
        RentalService rentalService = new RentalService(rentalRepo);

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
            /*
            customerService.findAll().forEach(System.out::println);

            System.out.println("Välj kund utifrån id");

            Long customerId = scanner.nextLong();

            Customer customer = customerService.findById(customerId);

            System.out.println("Vald kund: " + customer.getId() + " : " + customer.getName());

            //Välj BajaMaja

            bajaMajaService.findAll().forEach(System.out::println);

            System.out.println("Välj BajaMaja utifrån id");

            Long bajaMajaId = scanner.nextLong();

            BajaMaja bajaMaja = bajaMajaService.findById(bajaMajaId);

            System.out.println("Valt objekt: " + bajaMaja.getType() + " " + bajaMaja.getId() + " : " + bajaMaja.getName());

            //skapa Rental

            System.out.println("Skapar rental");

            Rental rental = rentalService.createRental(
                    customer,
                    bajaMaja.getType(),
                    bajaMaja.getId(),
                    LocalDateTime.of(2026,2,2,10,59),
                    LocalDateTime.of(2026,2,7,23,59),
                    bajaMaja.getRentalRate()
            );

            System.out.println("Skapade: " + rental);*/

            //--------------------------
            // Filtrera och sök BajaMaja
            //--------------------------

            //bajaMajaService.findAllFiltered("", false,0,0,false).forEach(System.out::println);

            //--------------------------
            // Filtrera och sök Kunder
            //--------------------------

            //customerService.findAllFiltered("sa",true).forEach(System.out::println);

            //-------------------------
            // Visa rentals
            //-------------------------

            /*System.out.println("Visar alla rentals av kund: " + customerService.findById(1L));

            rentalService.findAllByCustomerId(1L).forEach(System.out::println);*/

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
