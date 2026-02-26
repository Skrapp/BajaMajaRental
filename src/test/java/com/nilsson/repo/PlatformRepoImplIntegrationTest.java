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
}
