package com.nilsson.repo;

import com.nilsson.entity.Customer;
import com.nilsson.entity.rentable.BajaMaja;
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
            session.persist(customer);
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
            var tx = session.beginTransaction();
            List<Customer> result = session.createQuery(
                            "FROM Customer", Customer.class)
                    .getResultList();
            tx.commit();
            return result;
        }
    }
}
