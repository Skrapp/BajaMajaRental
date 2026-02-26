package com.nilsson.repo;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Color;
import com.nilsson.entity.rentable.Decoration;
import com.nilsson.entity.rentable.RentalObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DecorationRepoImpl implements DecorationRepo{
    private final SessionFactory sessionFactory;

    public DecorationRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Decoration decoration) {
        try(Session session = sessionFactory.openSession()){
            var tx = session.beginTransaction();

            //Om objektet ej tidigare blivit sparat har den ej fått ett id tilldelat, annars ska objektet uppdateras
            if(decoration.getId() == null) {
                session.persist(decoration);
            } else {
                session.merge(decoration);
            }
            tx.commit();
        }
    }

    @Override
    public Optional<Decoration> findById(long id) {
        try(Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.get(Decoration.class, id));
        }
    }

    @Override
    public List<Decoration> findAll() {
        try(Session session = sessionFactory.openSession()){
            var tx = session.beginTransaction();
            List<Decoration> result = session.createQuery(
                            "FROM Decoration", Decoration.class)
                    .getResultList();
            tx.commit();
            return result;
        }
    }

    /**
     *Filtrerar enligt de som är tillgängliga en specifik dag och ingående parametrar.
     * @param availableDate Vilket datum som artikeln ska vara tillgängligt
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param minimumRate Minsta kostnaden för en artikel.
     * @param maximumRate Största kostnaden för en artikel.
     * @return returnerar en lista av dekorationer enligt filtreringen
     */
    @Override
    public List<Decoration> findFilteredAvailableByDate(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate, List<Color> colors) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT d
                FROM Decoration d
                LEFT JOIN Rental r
                    ON r.rentalObjectId = d.id
                    AND r.rentalObjectType = :rentalType
                    AND r.startDate <= :availableDate
                    AND r.endDate >= :availableDate
                    AND r.returnDate IS NULL
                WHERE r.id IS NULL
                    AND d.rentalRate >= :minimumRate
                    AND d.rentalRate <= :maximumRate
                    AND (d.name LIKE :searchWord
                        OR d.description LIKE :searchWord)
                    AND d.color IN :colors
                """;

            Query<Decoration> query = session.createQuery(hql, Decoration.class)
                    .setParameter("rentalType", RentalObject.DECORATION)
                    .setParameter("availableDate", availableDate)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter( "searchWord" , "%"+searchWord+"%")
                    .setParameter("colors", colors);

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
    public List<Decoration> findFiltered(String searchWord, double minimumRate, double maximumRate, List<Color> colors) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT d
                FROM Decoration d
                WHERE d.rentalRate >= :minimumRate
                  AND d.rentalRate <= :maximumRate
                  AND (d.name LIKE :searchWord
                       OR d.description LIKE :searchWord)
                  AND d.color IN :colors
            """;

            Query<Decoration> query = session.createQuery(hql, Decoration.class)
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter("searchWord", " % "+searchWord+"%")
                    .setParameter("colors", colors);

            return query.getResultList();
        }
    }
}
