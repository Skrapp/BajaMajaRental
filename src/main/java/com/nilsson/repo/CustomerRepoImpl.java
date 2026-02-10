package com.nilsson.repo;

import com.nilsson.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class CustomerRepoImpl implements CustomerRepo{
    private final SessionFactory sessionFactory;

    public CustomerRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();

            if(customer.getId() == null) {
                session.persist(customer);
            }else {
                session.merge(customer);
            }

            tx.commit();
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        try(Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.get(Customer.class, id));
        }
    }

    @Override
    public List<Customer> findAll() {
        try(Session session = sessionFactory.openSession()) {
            List<Customer> result = session.createQuery(
                            "FROM Customer", Customer.class)
                    .getResultList();
            return result;
        }
    }

    /**
     *
     * @param searchWord att söka efter i namn eller mail, skriv "" för att inte söka efter något specifikt
     * @param requireRentals ange true för att få kunder som har minst en uthyrning dokumenterad
     * @return En filtrerad lista med kunder
     */
    @Override
    public List<Customer> findAllFiltered(String searchWord, boolean requireRentals){
        try(Session session = sessionFactory.openSession()){
            String sql;
            if(requireRentals) {
                sql = """
                        SELECT c.*
                        FROM customers c
                            INNER JOIN rentals r ON c.id = r.customer_id
                        WHERE c.name like :searchWord
                            OR c.email like :searchWord
                        GROUP BY c.id
                        """;
            }else {
                sql = """
                        SELECT c.*
                        FROM customers c
                        WHERE c.name like :searchWord
                            OR c.email like :searchWord
                        """;
            }

            return session.createNativeQuery(sql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }
}
