package com.nilsson.repo;

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
}
