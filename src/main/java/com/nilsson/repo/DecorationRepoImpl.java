package com.nilsson.repo;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Color;
import com.nilsson.entity.rentable.Decoration;
import com.nilsson.entity.rentable.RentalObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

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
    public List<Decoration> findAllFiltered(String searchWord, boolean requireAvailableToday, double minimumRate, double maximumRate, List<Color> colors) {
        try(Session session = sessionFactory.openSession()) {
            String sql = """
                
                    SELECT d.*
                FROM decorations d
                """;
            String joinSQL = "";
            List<String> whereSQLList = new ArrayList<>();

            //Ser vilka parametrar som är aktuella
            if(requireAvailableToday){
                joinSQL = """
                    LEFT JOIN rentals r
                        ON r.rental_Object_id = d.id
                        AND r.rental_Object_Type = :rentalType
                        AND r.start_Date < CURRENT_TIMESTAMP
                        AND NOT r.returned
                """;
                whereSQLList.add("r.id IS NULL");
            }
            if (minimumRate > 0){
                whereSQLList.add("d.rental_rate >= :minimumRate");
            }
            if (maximumRate > 0){
                whereSQLList.add("d.rental_rate <= :maximumRate");
            }
            if(!searchWord.isBlank()){
                whereSQLList.add("(d.name  like :searchWord OR d.description like :searchWord)");
            }
            boolean filterColors = colors != null && !colors.isEmpty() && colors.equals(Arrays.stream(Color.values()).toList());
            if (filterColors) {
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
            if (minimumRate > 0){
                query.setParameter("minimumRate", minimumRate);
            }
            if (maximumRate > 0){
                query.setParameter("maximumRate", maximumRate);
            }
            if(!searchWord.isBlank()){
                query.setParameter("searchWord", '%'+searchWord+'%');
            }

            if(filterColors){
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
