package com.nilsson;

import com.nilsson.repo.*;
import com.nilsson.service.*;
import com.nilsson.ui.UIManager;
import com.nilsson.ui.UIState;
import com.nilsson.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Repos
        CustomerRepo customerRepo = new CustomerRepoImpl(sessionFactory);
        BajaMajaRepo bajaMajaRepo = new BajaMajaRepoImpl(sessionFactory);
        RentalRepo rentalRepo = new RentalRepoImpl(sessionFactory);
        DecorationRepo decorationRepo = new DecorationRepoImpl(sessionFactory);
        PlatformRepo platformRepo = new PlatformRepoImpl(sessionFactory);

        // Services
        CustomerService customerService = new CustomerService(customerRepo);
        BajaMajaService bajaMajaService = new BajaMajaService(bajaMajaRepo);
        RentalService rentalService = new RentalService(rentalRepo);
        DecorationService decorationService = new DecorationService(decorationRepo);
        PlatformService platformService = new PlatformService(platformRepo, bajaMajaRepo);

        UIManager uiManager = new UIManager
                (new BufferedReader(new InputStreamReader(System.in)),
                        customerService,
                        rentalService,
                        bajaMajaService,
                        decorationService,
                        platformService);
        try {
            uiManager.run(UIState.LIST_RENTAL_OBJECTS);

        } catch (IOException e){
            throw new RuntimeException(e);
        }
        finally {
            HibernateUtil.shutdown();
        }
    }
}
