package com.nilsson.repo;

import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RentalRepoImpl implements RentalRepo{
    private final SessionFactory sessionFactory;

    public RentalRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Rental rental) {
        try(Session session = sessionFactory.openSession()){
            var tx = session.beginTransaction();
            if(rental.getId() == null) {
                session.persist(rental);
            } else {
                session.merge(rental);
            }
            tx.commit();
        }
    }

    @Override
    public Optional<Rental> findById(long id) {
        try(Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.get(Rental.class, id));
        }
    }

    /**
     * Ser om uthyrningobjektet är redan uthyrt under den period som efterfrågas.
     * @param rentalObjectType
     * @param rentalObjectId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public boolean availableByRentalObjectAndDate(RentalObject rentalObjectType, long rentalObjectId, LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = sessionFactory.openSession()) {
            String sql = """
                    SELECT COUNT(*)
                    FROM rentals
                    WHERE rental_object_type = :rentalObjectType
                        AND rental_object_id = :rentalObjectId
                        AND return_date IS NULL
                        AND start_date < :endDate
                        AND end_date > :startDate
                    """;

            Number count = (Number) session.createNativeQuery(sql)
                    .setParameter("rentalObjectType", rentalObjectType.name())
                    .setParameter("rentalObjectId", rentalObjectId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getSingleResult();

            return count.longValue() == 0;
        }
    }

    @Override
    public List<Rental> findAllRentalsByCustomerId(long customerId) {
        try(Session session = sessionFactory.openSession()){
            String sql = """
                    SELECT r
                    FROM Rental r
                    WHERE r.customer.id = :customerId
                    """;

            List<Rental> result =  session.createQuery(sql, Rental.class)
                    .setParameter("customerId", customerId)
                    .getResultList();

            return result;
        }
    }

    @Override
    public List<Rental> findActiveRentalsByCustomerId(long customerId) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT r
                    FROM Rental r
                    WHERE r.customer.id = :customerId
                        AND r.startDate < CURRENT_TIMESTAMP
                        AND r.returnDate IS NULL
                    """;

            Query<Rental> query = session.createQuery(hql, Rental.class)
                    .setParameter("customerId", customerId);

            return query.getResultList();
        }
    }

    @Override
    public List<Rental> findFutureRentalsByCustomerId(long customerId) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT r
                    FROM Rental r
                    WHERE r.customer.id = :customerId
                        AND r.endDate > CURRENT_TIMESTAMP
                        AND r.returnDate IS NULL
                    """;

            Query<Rental> query = session.createQuery(hql, Rental.class)
                    .setParameter("customerId", customerId);

            return query.getResultList();
        }
    }

    @Override
    public List<Rental> findLateRentalsByCustomerId(long customerId) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT r
                    FROM Rental r
                    WHERE r.customer.id = :customerId
                        AND r.endDate < CURRENT_TIMESTAMP
                        AND r.returnDate IS NULL
                    """;

            Query<Rental> query = session.createQuery(hql, Rental.class)
                    .setParameter("customerId", customerId);

            return query.getResultList();
        }
    }

    @Override
    public List<Rental> findReturnedRentalsByCustomerId(long customerId) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT r
                    FROM Rental r
                    WHERE r.customer.id = :customerId
                        AND r.returnDate IS NOT NULL
                    """;

            Query<Rental> query = session.createQuery(hql, Rental.class)
                    .setParameter("customerId", customerId);

            return query.getResultList();
        }
    }

    @Override
    public List<Rental> findCanceledRentalsByCustomerId(long customerId) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT r
                    FROM Rental r
                    WHERE r.customer.id = :customerId
                        AND r.returnDate IS NOT NULL
                        AND r.returnDate < r.startDate
                    """;

            Query<Rental> query = session.createQuery(hql, Rental.class)
                    .setParameter("customerId", customerId);

            return query.getResultList();
        }
    }

    @Override
    public List<Rental> findNotReturnedRentalsByCustomerId(long customerId) {
        try(Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT r
                    FROM Rental r
                    WHERE r.customer.id = :customerId
                        AND r.returnDate IS NULL
                    """;

            Query<Rental> query = session.createQuery(hql, Rental.class)
                    .setParameter("customerId", customerId);

            return query.getResultList();
        }
    }

    @Override
    public List<Rental> findFutureRentalsByRentalObjectId(RentalObject rentalObjectType, long rentalObjectId) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                    SELECT *
                    FROM rentals 
                    WHERE rental_object_id = :rentalObjectId
                        AND rental_object_type = :rentalObjectType
                        AND return_date IS NULL
                        AND end_date > CURRENT_TIMESTAMP
                    """;

            Query<Rental> query =  session.createQuery(hql, Rental.class)
                    .setParameter("rentalObjectId", rentalObjectId)
                    .setParameter("rentalObjectType", rentalObjectType.name());

            return query.getResultList();
        }
    }
}
