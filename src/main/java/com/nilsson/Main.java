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
            System.out.println("=== Wigell Cinema Demo ===");

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

            customerService.findAllFiltered("sa",true).forEach(System.out::println);

            System.out.println("Välj kund utifrån id");

            Long customerId = scanner.nextLong();

            Customer customer = customerService.findById(customerId);

            System.out.println("Vald kund: " + customer.getId() + " : " + customer.getName());

            //Välj BajaMaja

            bajaMajaService.findAll().forEach(System.out::println);

            System.out.println("Välj BajaMaja utifrån id");

            Long bajaMajaId = scanner.nextLong();

            BajaMaja bajaMaja = bajaMajaService.findById(bajaMajaId);

            System.out.println("Valt objekt: " + bajaMaja.getRentalObjectType() + " " + bajaMaja.getId() + " : " + bajaMaja.getName());

            //skapa Rental

            System.out.println("Skapar rental");

            Rental rental = rentalService.createRental(
                    customer,
                    bajaMaja.getRentalObjectType(),
                    bajaMaja.getId(),
                    LocalDateTime.of(2026,2,18,23,59),
                    LocalDateTime.of(2026,2,20,23,59),
                    bajaMaja.getRentalRate()
            );

            System.out.println("Skapade: " + rental);

            //--------------------------
            // Filtrera BajaMaja
            //--------------------------

            //--------------------------
            // Filtrera Kunder
            //--------------------------

            //--------------------------
            // Sök BajaMaja
            //--------------------------

            //--------------------------
            // Sök kund
            //--------------------------

            //-------------------------
            // Visa rentals
            //-------------------------

            // ---------------------------
            // 2) Skapa demo-film + visning
            // ---------------------------
            /*System.out.println("\n--- Skapa demo-visning ---");

            Movie movie = new Movie("The Conjuring (demo)", 112);
            movie.setDetails(new MovieDetails("Demo-synopsis", "James Wan", "EN", 15));

            Show show = new Show(LocalDateTime.now().plusDays(1), 1);
            movie.addShow(show);

            // Spara movie -> cascade sparar details + show
            movieRepo.save(movie);

            System.out.println("✅ Film sparad med ID: " + movie.getId());
            System.out.println("✅ Visning sparad med ID: " + show.getId() +
                    " (salong " + show.getScreenNumber() + ", start " + show.getStartsAt() + ")");

            // ---------------------------
            // 3) Köp biljett = gör bokning
            // ---------------------------
            System.out.println("\n--- Köp biljett ---");
            System.out.print("Välj platsnummer (t.ex. 12): ");
            int seatNo = Integer.parseInt(scanner.nextLine());

            // Enkel QR-kod för demo: unik sträng
            String qrCode = "QR-" + UUID.randomUUID();

            Booking booking = bookingService.bookSeat(show, seatNo, customer.getName(), qrCode);

            System.out.println("\n🎟️ Biljett köpt!");
            System.out.println("Film: " + movie.getTitle());
            System.out.println("Visning: " + show.getStartsAt() + " (salong " + show.getScreenNumber() + ")");
            System.out.println("Plats: " + booking.getSeatNo());
            System.out.println("Kund: " + booking.getCustomerName());
            System.out.println("QR: " + booking.getTicket().getQrCode());
            System.out.println("Booking-ID: " + booking.getId());*/

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
