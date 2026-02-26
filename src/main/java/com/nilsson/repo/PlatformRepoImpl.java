package com.nilsson.repo;

import com.nilsson.entity.rentable.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PlatformRepoImpl implements PlatformRepo{
    private final SessionFactory sessionFactory;

    public PlatformRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Platform platform) {
        try(Session session = sessionFactory.openSession()){
            var tx = session.beginTransaction();

            //Om objektet ej tidigare blivit sparat har den ej fått ett id tilldelat, annars ska objektet uppdateras
            if(platform.getId() == null) {
                session.persist(platform);
            } else {
                session.merge(platform);
            }
            tx.commit();
        }
    }

    @Override
    public Optional<Platform> findById(Long id) {
        try(Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.get(Platform.class, id));
        }
    }

    @Override
    public List<Platform> findAll() {
        try(Session session = sessionFactory.openSession()){
            var tx = session.beginTransaction();
            List<Platform> result = session.createQuery(
                            "FROM Platform", Platform.class)
                    .getResultList();
            tx.commit();
            return result;
        }
    }

    /**
     *Filtrerar enligt de som är tillgängliga en specifik dag, är kompatibla med specifik BajaMaja och ingående parametrar.
     * @param availableDate Vilket datum som artikeln ska vara tillgängligt
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @param bajaMajaId Id på BajaMaja som plattformen ska vara kompatibel med
     * @return returnerar en lista av plattformar enligt filtreringen
     */
    @Override
    public List<Platform> findFilteredAvailableByDateAndByBajaMaja(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate, Long bajaMajaId) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT p
                    FROM Platform p
                    LEFT JOIN Rental r
                        ON r.rentalObjectId = p.id
                        AND r.rentalObjectType = :rentalType
                        AND r.startDate <= :availableDate
                        AND r.endDate >= :availableDate
                        AND r.returnDate IS NULL
                    INNER JOIN p.bajamajas b
                    WHERE r.id IS NULL
                        AND p.rentalRate >= :minimumRate
                        AND p.rentalRate <= :maximumRate
                        AND (p.name like :searchWord
                            OR p.description like :searchWord)
                        AND b.id = :bajaMajaId
                """;

            Query<Platform> query = session.createQuery(hql, Platform.class)
                    .setParameter("rentalType", RentalObject.PLATFORM)
                    .setParameter("availableDate", availableDate)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .setParameter("bajaMajaId", bajaMajaId);

            return query.getResultList();
        }
    }

    /**
     *Filtrerar enligt de som är tillgängliga en specifik dag och ingående parametrar.
     * @param availableDate Vilket datum som artikeln ska vara tillgängligt
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @return returnerar en lista av plattformar enligt filtreringen
     */
    @Override
    public List<Platform> findFilteredAvailableByDate(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT p
                    FROM Platform p
                    LEFT JOIN Rental r
                        ON r.rentalObjectId = p.id
                        AND r.rentalObjectType = :rentalType
                        AND r.startDate <= :availableDate
                        AND r.endDate >= :availableDate
                        AND r.returnDate IS NULL
                    WHERE r.id IS NULL
                        AND p.rentalRate >= :minimumRate
                        AND p.rentalRate <= :maximumRate
                        AND (p.name like :searchWord
                            OR p.description like :searchWord)
                """;

            Query<Platform> query = session.createQuery(hql, Platform.class)
                    .setParameter("rentalType", RentalObject.PLATFORM)
                    .setParameter("availableDate", availableDate)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", '%'+searchWord+'%');

            return query.getResultList();
        }
    }

    /**
     *Filtrerar enligt de som är kompatibla med specifik BajaMaja och ingående parametrar.
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @param bajaMajaId Id på BajaMaja som plattformen ska vara kompatibel med
     * @return returnerar en lista av plattformar enligt filtreringen
     */
    @Override
    public List<Platform> findFilteredByBajaMaja(String searchWord, double minimumRate, double maximumRate, Long bajaMajaId) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT distinct p
                    FROM Platform p
                    INNER JOIN p.bajamajas b
                    WHERE p.rentalRate >= :minimumRate
                        AND p.rentalRate <= :maximumRate
                        AND (p.name like :searchWord
                            OR p.description like :searchWord)
                        AND b.id = :bajaMajaId
                """;

            Query<Platform> query = session.createQuery(hql, Platform.class)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .setParameter("bajaMajaId", bajaMajaId);

            return query.getResultList();
        }
    }

    /**
     *Filtrerar enligt ingående parametrar.
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @return returnerar en lista av plattformar enligt filtreringen
     */
    @Override
    public List<Platform> findFiltered(String searchWord, double minimumRate, double maximumRate) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT p
                    FROM Platform p
                    WHERE p.rentalRate >= :minimumRate
                        AND p.rentalRate <= :maximumRate
                        AND (p.name like :searchWord
                            OR p.description like :searchWord)
                """;

            Query<Platform> query = session.createQuery(hql, Platform.class)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", '%'+searchWord+'%');

            return query.getResultList();
        }
    }
}
