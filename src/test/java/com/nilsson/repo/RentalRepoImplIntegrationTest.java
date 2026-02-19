package com.nilsson.repo;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import util.HibernateUtilTest;

public class RentalRepoImplIntegrationTest {
    private static SessionFactory sessionFactory;



    @BeforeAll
    static void beforeAll() {
        sessionFactory = HibernateUtilTest.getSessionFactory();
    }

    @AfterAll
    static void afterAll() {
        HibernateUtilTest.shutdown();
    }

    //Hela processen: Spara objekt, hyra objekt, hyr objekt annat datum, hyr samma datum, lämna tillbaka, hyr samma datum

}
