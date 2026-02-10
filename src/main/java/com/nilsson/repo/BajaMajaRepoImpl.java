package com.nilsson.repo;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.RentalObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

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
     *Filtrerar enligt parametrar.
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param requireAvailableToday Sätt true om det endast ska lista artiklar som inte är bokade idag
     * @param minimumRate Minsta kostnaden av artikel. Sätt 0 för att inte filtrera enligt minsta
     * @param maximumRate Största kostnaden för en artikel. Sätt 0 för att inte filtrera enligt största
     * @param requireHandicap Sätt true för att endast inkludera handikappanpassade bajamajor
     * @return returnerar en lista av BajaMajas enligt filtreringen
     */
    @Override
    public List<BajaMaja> findAllFiltered(String searchWord, boolean requireAvailableToday, double minimumRate, double maximumRate, boolean requireHandicap) {


        try(Session session = sessionFactory.openSession()) {
            String sql = """
                SELECT b.*
                FROM bajamajas b
                """;
            String joinSQL = "";
            List<String> whereSQLList = new ArrayList<>();

            //Ser vilka parametrar som är aktuella
            if(requireAvailableToday){
                joinSQL = """
                LEFT JOIN rentals r
                ON r.rental_Object_Type = :rentalType
                AND r.rental_Object_Id = b.id
                AND r.start_Date < CURRENT_TIMESTAMP
                AND NOT r.returned
                """;
                whereSQLList.add("r.id IS NULL");
            }
            if (minimumRate > 0){
                whereSQLList.add("b.rental_rate >= :minimumRate");
            }
            if (maximumRate > 0){
                whereSQLList.add("b.rental_rate <= :maximumRate");
            }
            if(!searchWord.isBlank()){
                whereSQLList.add("(b.name  like :searchWord OR b.description like :searchWord)");
            }
            if (requireHandicap) {
                whereSQLList.add("b.handicap = true");
            }

            //Slår ihop till en SQL query
            sql = sql
                    + joinSQL
                    + (whereSQLList.isEmpty() ? "" : " WHERE " + String.join(" AND ", whereSQLList));

            NativeQuery<BajaMaja> query = session.createNativeQuery(sql, BajaMaja.class);

            //Ställer in parametrar
            if(requireAvailableToday){
                query.setParameter("rentalType", RentalObject.BAJAMAJA);
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

            return query.getResultList();
    }
}
}
