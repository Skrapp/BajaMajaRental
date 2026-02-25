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

    @Override
    public List<Decoration> findFilteredDecorations(String searchWord, double minimumRate, double maximumRate, List<Color> colors) {
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
                    .setParameter("searchWord", "%"+searchWord+"%")
                    .setParameter("colors", colors);

            return query.getResultList();
            /*String sql = """
                SELECT d.*
                FROM decorations d
                WHERE d.rental_rate >= :minimumRate
                    AND d.rental_rate <= :maximumRate
                    AND (d.name  like :searchWord OR d.description like :searchWord)
                    AND d.color IN :colors
                """;



            List<String> colorStrings = colors.stream()
                    .map(Enum::name)
                    .toList();

            NativeQuery<Decoration> query = session.createNativeQuery(sql, Decoration.class)
                    .setParameter("minimumRate" , minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .setParameter( "searchWord", "%"+searchWord+"%")
                    .setParameter("colors", colorStrings);

            return query.getResultList();*/
            }

    }

    @Override
    public List<Decoration> findAllFiltered(String searchWord, Boolean requireAvailableToday, Double minimumRate, Double maximumRate, List<Color> colors) {
        try(Session session = sessionFactory.openSession()) {
            String sql = """
                SELECT d.*
                FROM decorations d
                """;
            String joinSQL = "";
            List<String> whereSQLList = new ArrayList<>();

            //Ser vilka parametrar som är aktuella
            if(requireAvailableToday != null){
                joinSQL = """
                    LEFT JOIN rentals r
                        ON r.rental_Object_id = d.id
                        AND r.rental_Object_Type = :rentalType
                        AND r.start_Date < CURRENT_TIMESTAMP
                        AND r.return_date IS NOT NULL
                """;
                whereSQLList.add("r.id IS NULL");
            }
            if (minimumRate != null){
                whereSQLList.add("d.rental_rate >= :minimumRate");
            }
            if (maximumRate != null){
                whereSQLList.add("d.rental_rate <= :maximumRate");
            }
            if(searchWord != null){
                whereSQLList.add("(d.name  like :searchWord OR d.description like :searchWord)");
            }

            if (colors != null) {
                whereSQLList.add("(d.color = :colors)");
            }

            //Slår ihop till en SQL query
            sql = sql
                    + joinSQL
                    + (whereSQLList.isEmpty() ? "" : " WHERE " + String.join(" AND ", whereSQLList));

            NativeQuery<Decoration> query = session.createNativeQuery(sql, Decoration.class);

            //Ställer in parametrar
            if(requireAvailableToday){
                query.setParameter("rentalType", RentalObject.DECORATION.name());
            }
            if (minimumRate != null){
                query.setParameter("minimumRate", minimumRate);
            }
            if (maximumRate != null){
                query.setParameter("maximumRate", maximumRate);
            }
            if(searchWord != null){
                query.setParameter("searchWord", '%'+searchWord+'%');
            }

            if(colors != null){
                List<String> colorStrings = colors.stream()
                        .map(String::valueOf)
                        .toList();
                String colorSQL = String.join(" OR d.color = ", colorStrings);
                query.setParameter("colors", colorSQL);
            }

            return query.getResultList();
        }
    }
}
