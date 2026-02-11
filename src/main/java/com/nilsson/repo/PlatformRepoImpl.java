package com.nilsson.repo;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Platform;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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
    public List<Platform> findAllFiltered(String searchWord, boolean requireAvailable, double minimumRate, double maximumRate, Long bajaMajaId) {
        return List.of();
    }
}
