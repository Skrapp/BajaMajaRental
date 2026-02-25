package com.nilsson.repo;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Platform;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.HibernateUtilTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PlatformRepoImplIntegrationTest {
    private static SessionFactory sessionFactory;
    private BajaMajaRepoImpl bajaMajaRepo;
    private PlatformRepoImpl platformRepo;

    @BeforeAll
    static void beforeAll() {
        sessionFactory = HibernateUtilTest.getSessionFactory();
    }

    @AfterAll
    static void afterAll() {
        HibernateUtilTest.shutdown();
    }
    //Hela processen: Spara objekt, hyra objekt, hyr objekt annat datum, hyr samma datum, lämna tillbaka, hyr samma datum

    @BeforeEach
    void setUp() {
        bajaMajaRepo = new BajaMajaRepoImpl(sessionFactory);
        platformRepo = new PlatformRepoImpl(sessionFactory);
    }

    @Test
    void savePlatformWithMultipleBajaMajas() {
        //Skapa två Bajamajor och sparar till db
        BajaMaja b1 = new BajaMaja("Classic", 200, 1);
        BajaMaja b2 = new BajaMaja("Deluxe", 400, 2);

        bajaMajaRepo.save(b1);
        bajaMajaRepo.save(b2);

        assertNotNull(b1.getId());
        assertNotNull(b2.getId());

        // Skapa Platform och koppla BajaMajor
        Platform platform = new Platform("FestivalPlatform", 1500);

        platform.addBajaMaja(b1);
        platform.addBajaMaja(b2);

        platformRepo.save(platform);

        assertNotNull(platform.getId());
        assertEquals(2, platform.getBajamajas().size(),
                "Plattformen ska ha två kopplade BajaMajor");
    }
}
