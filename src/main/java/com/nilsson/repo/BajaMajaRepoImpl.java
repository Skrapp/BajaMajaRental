package com.nilsson.repo;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.RentalObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BajaMajaRepoImpl implements BajaMajaRepo {

    private final SessionFactory sessionFactory;

    public BajaMajaRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(BajaMaja bajaMaja) {
        try(Session session = sessionFactory.openSession()){
            var tx = session.beginTransaction();

            //Om objektet ej tidigare blivit sparat har den ej fått ett id tilldelat, annars ska objektet uppdateras
            if(bajaMaja.getId() == null) {
                session.persist(bajaMaja);
            } else {
                session.merge(bajaMaja);
            }
            tx.commit();
        }
    }

    @Override
    public Optional<BajaMaja> findById(long id) {
        try(Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.get(BajaMaja.class, id));
        }
    }

    /**
     * Hämtar alla BajaMajor
     * @return returnerar en lista av BajaMajor
     */
    @Override
    public List<BajaMaja> findAll() {
        try(Session session = sessionFactory.openSession()){
            var tx = session.beginTransaction();
            List<BajaMaja> result = session.createQuery(
                    "SELECT b FROM BajaMaja b", BajaMaja.class)
                    .getResultList();
            tx.commit();
            return result;
        }
    }

    /**
     *Filtrerar enligt de som är tillgängliga en specifik dag, är handikappanpassade och ingående parametrar.
     * @param availableDate Vilket datum som objektet ska vara tillgängligt
     * @param searchWord Söker i beskrivning och namn. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @return returnerar en lista av BajaMajas enligt filtreringen
     */
    @Override
    public List<BajaMaja> findFilteredAvailableByDateAndHandicap(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT b
                FROM BajaMaja b
                LEFT JOIN Rental r
                    ON r.rentalObjectId = b.id
                    AND r.rentalObjectType = :rentalType
                    AND r.startDate <= :availableDate
                    AND r.endDate >= :availableDate
                    AND r.returnDate IS NULL
                WHERE r.id IS NULL
                    AND b.rentalRate >= :minimumRate
                    AND b.rentalRate <= :maximumRate
                    AND b.name like :searchWord
                    AND b.handicap
                """;

            Query<BajaMaja> query = session.createQuery(hql, BajaMaja.class)
                    .setParameter("rentalType", RentalObject.BAJAMAJA)
                    .setParameter("availableDate", availableDate)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", '%'+searchWord+'%');

            return query.getResultList();
        }
    }

    /**
     *Filtrerar enligt de som är tillgängliga en specifik dag och ingående parametrar.
     * @param availableDate Vilket datum som objektet ska vara tillgängligt
     * @param searchWord Söker i beskrivning och namn. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @return returnerar en lista av BajaMajas enligt filtreringen
     */
    @Override
    public List<BajaMaja> findFilteredAvailableByDate(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT b
                FROM BajaMaja b
                LEFT JOIN Rental r
                    ON r.rentalObjectId = b.id
                    AND r.rentalObjectType = :rentalType
                    AND r.startDate <= :availableDate
                    AND r.endDate >= :availableDate
                    AND r.returnDate IS NULL
                WHERE r.id IS NULL
                    AND b.rentalRate >= :minimumRate
                    AND b.rentalRate <= :maximumRate
                    AND b.name like :searchWord
                """;

            Query<BajaMaja> query = session.createQuery(hql, BajaMaja.class)
                    .setParameter("rentalType", RentalObject.BAJAMAJA)
                    .setParameter("availableDate", availableDate)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", '%'+searchWord+'%');

            return query.getResultList();
        }
    }

    /**
     *Filtrerar enligt de som är handikappanpassade och ingående parametrar.
     * @param searchWord Söker i beskrivning och namn. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @return returnerar en lista av BajaMajas enligt filtreringen
     */
    @Override
    public List<BajaMaja> findFilteredHandicap(String searchWord, double minimumRate, double maximumRate) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT b
                FROM BajaMaja b
                WHERE b.rentalRate >= :minimumRate
                    AND b.rentalRate <= :maximumRate
                    AND b.name like :searchWord
                    AND b.handicap
                """;

            Query<BajaMaja> query = session.createQuery(hql, BajaMaja.class)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", '%'+searchWord+'%');

            return query.getResultList();
        }
    }

    /**
     *Filtrerar enligt ingående parametrar.
     * @param searchWord Söker i beskrivning och namn. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @return returnerar en lista av BajaMajas enligt filtreringen
     */
    @Override
    public List<BajaMaja> findFiltered(String searchWord, double minimumRate, double maximumRate) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT b
                FROM BajaMaja b
                WHERE b.rentalRate >= :minimumRate
                    AND b.rentalRate <= :maximumRate
                    AND b.name like :searchWord
                """;

            Query<BajaMaja> query = session.createQuery(hql, BajaMaja.class)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", '%'+searchWord+'%');

            return query.getResultList();
        }
    }
}
