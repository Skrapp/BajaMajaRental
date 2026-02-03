package com.nilsson.repo;

import com.nilsson.entity.Rental;
import com.nilsson.entity.rentable.RentalObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class RentalRepoImpl implements RentalRepo{
    private SessionFactory sessionFactory;

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
        return List.of();
    }

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
                        AND start_date < :endDate
                        AND end_date > :startDate
                        AND returned = false
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
