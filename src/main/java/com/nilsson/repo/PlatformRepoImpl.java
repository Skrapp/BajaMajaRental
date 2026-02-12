package com.nilsson.repo;

import com.nilsson.entity.rentable.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

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

    @Override
    public List<Platform> findAllFiltered(String searchWord, boolean requireAvailableToday, double minimumRate, double maximumRate, Long bajaMajaId) {
        try(Session session = sessionFactory.openSession()) {
            String sql = """
                
                    SELECT p.*
                FROM platforms p
                """;
            String joinSQL = "";
            List<String> whereSQLList = new ArrayList<>();

            //Ser vilka parametrar som är aktuella
            if(requireAvailableToday){
                joinSQL = """
                    LEFT JOIN rentals r
                        ON r.rental_Object_id = p.id
                        AND r.rental_Object_Type = :rentalType
                        AND r.start_Date < CURRENT_TIMESTAMP
                        AND NOT r.returned
                """;
                whereSQLList.add("r.id IS NULL");
            }
            if (minimumRate > 0){
                whereSQLList.add("p.rental_rate >= :minimumRate");
            }
            if (maximumRate > 0){
                whereSQLList.add("p.rental_rate <= :maximumRate");
            }
            if(!searchWord.isBlank()){
                whereSQLList.add("(p.name  like :searchWord OR p.description like :searchWord)");
            }
            if (bajaMajaId > 0) {
                joinSQL = joinSQL + """
                INNER JOIN join_platforms_bajamajas pb
                ON p.id = pb.platform_id
                AND pb.bajamaja_id = :bajaMajaId
                """;
            }

            //Slår ihop till en SQL query
            sql = sql
                    + joinSQL
                    + (whereSQLList.isEmpty() ? "" : " WHERE " + String.join(" AND ", whereSQLList));

            NativeQuery<Platform> query = session.createNativeQuery(sql, Platform.class);

            //Ställer in parametrar
            if(requireAvailableToday){
                query.setParameter("rentalType", RentalObject.PLATFORM.name());
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
            if(bajaMajaId > 0){
                query.setParameter("bajaMajaId", bajaMajaId);
            }

            return query.getResultList();
        }
    }
}
