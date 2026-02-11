package com.nilsson.util;

import com.nilsson.entity.*;
import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Decoration;
import com.nilsson.entity.rentable.Platform;
import com.nilsson.entity.rentable.RentalObject;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {

    private static final String PROPERTIES_FILE = "hibernate.properties";

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() { }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (!SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }

    private static SessionFactory buildSessionFactory() {

        try {
            Properties properties = new Properties();

            try (InputStream in =
                         HibernateUtil.class
                                 .getClassLoader()
                                 .getResourceAsStream(PROPERTIES_FILE)) {

                if (in == null) {
                    throw new IllegalStateException(
                            PROPERTIES_FILE + " not found in resources"
                    );
                }

                properties.load(in);
            }

            Configuration configuration = new Configuration();
            configuration.setProperties(properties);

            configuration.addAnnotatedClass(Customer.class);
            configuration.addAnnotatedClass(BajaMaja.class);
            configuration.addAnnotatedClass(Decoration.class);
            configuration.addAnnotatedClass(Platform.class);
            configuration.addAnnotatedClass(Rental.class);
            configuration.addAnnotatedClass(RentalObject.class);

            ServiceRegistry serviceRegistry =
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties())
                            .build();

            return configuration.buildSessionFactory(serviceRegistry);

        } catch (Exception e) {

            throw new ExceptionInInitializerError(
                    "Failed to build SessionFactory: " + e.getMessage()
            );
        }
    }
}
