package com.nilsson.repo;

import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<Rental> findAllByCustomerId(Long customerId) {
        System.out.println("WIP");
        try(Session session = sessionFactory.openSession()){
            String sql = """
                    SELECT r.*
                    FROM rentals r
                    INNER JOIN customers c ON r.customer_id = c.id
                    WHERE r.customer_id = :customerId
                    """;


            List<Rental> result =  session.createNativeQuery(sql, Rental.class)
                    .setParameter("customerId", customerId)
                    .getResultList();

            return result;
        }
    }

    /**
     * Ser om uthyrningobjektet är redan uthyrt under den period som efterfrågas.
     * TODO istället för att bara se på id göra en funktion för att se ifall den typen (namnet) finns tillgänglig för att kunna ha flera av samma modell
     * @param rentalObjectType
     * @param rentalObjectId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public boolean availableByRentalObjectAndDate(RentalObject rentalObjectType, long rentalObjectId, LocalDateTime startDate, LocalDateTime endDate) {

        //Finns detta rentalObjectType id som rental
        // & krockar datumen (är existing.startdate innan request.enddate och existing.enddate efter request.startdate)
        try (Session session = sessionFactory.openSession()) {

            String sql = """
                    SELECT COUNT(*)
                    FROM rentals
                    WHERE rental_object_type = :rentalObjectType
                        AND rental_object_id = :rentalObjectId
                        AND NOT returned
                        AND start_date < :endDate
                        AND end_date > :startDate
                    """;

            Number count = (Number) session.createNativeQuery(sql)
                    .setParameter("rentalObjectType", rentalObjectType)
                    .setParameter("rentalObjectId", rentalObjectId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getSingleResult();

            return count.longValue() == 0;
        }
    }
}
