package com.nilsson.repo;

import com.nilsson.entity.Customer;
import com.nilsson.entity.rentable.BajaMaja;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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



    private String requireAvailable(){
        return "";
    }

    private String addRequireHandicap(){
        return "AND b.handicap = true";
    }


    @Override
    public List<BajaMaja> findAllFiltered(String searchWord, boolean requireAvailable, double minimumRate, double maximumRate, boolean requireHandicap) {
        try(Session session = sessionFactory.openSession()) {
            String sql;
            if (requireAvailable) {
                sql = """
                        SELECT b.*
                        FROM bajamajas b
                            INNER JOIN rentals r ON b.id = r.customer_id
                        WHERE (b.name like :searchWord
                            OR b.description like :searchWord)
                        """
                        + (requireHandicap ? addRequireHandicap() : "") +
                        """
                        GROUP BY b.id
                        """;
            } else {
                sql = """
                        SELECT b.*
                        FROM bajamajas b
                        WHERE (b.name like :searchWord
                            OR b.description like :searchWord)
                            AND b.rental_rate >= :minimumRate
                            AND b.rental_rate <= :maximumRate
                        """ + (requireHandicap ? addRequireHandicap() : "");
            }

            List<BajaMaja> result = session.createNativeQuery(sql, BajaMaja.class)
                    .setParameter("searchWord", '%' + searchWord + '%')
                    .setParameter("minimumRate", minimumRate)
                    .setParameter("maximumRate", maximumRate)
                    .getResultList();
            return result;
        }
    }
}
