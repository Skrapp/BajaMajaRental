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
     * Filtrerar enligt kunder med någon registrerad uthyrningar och ingående parametrar och sorterar enligt kundnamn i fallande ordning
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredWithAnyRentalsSortByNameDesc(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT distinct c
                        FROM Customer c
                        INNER JOIN c.rentals r
                        WHERE (c.name like :searchWord
                            OR c.email like :searchWord)
                        ORDER BY c.name DESC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }

    /**
     * Filtrerar enligt kunder med någon registrerad uthyrningar och ingående parametrar och sorterar enligt kundnamn i stigande ordning
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredWithAnyRentalsSortByNameAsc(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT distinct c
                        FROM Customer c
                        INNER JOIN c.rentals r
                        WHERE (c.name like :searchWord
                            OR c.email like :searchWord)
                        ORDER BY c.name ASC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }

    /**
     * Filtrerar enligt kunder med aktiva uthyrningar och ingående parametrar och sorterar enligt namn i stigande ordning
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredWithActiveRentalsSortByNameAsc(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT distinct c
                        FROM Customer c
                        INNER JOIN c.rentals r
                        WHERE (c.name like :searchWord
                            OR c.email like :searchWord)
                            AND r.returnDate IS NULL
                            AND r.startDate < CURRENT TIMESTAMP
                        ORDER BY c.name ASC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }

    /**
     * Filtrerar enligt kunder med aktiva uthyrningar och ingående parametrar och sorterar enligt namn i fallande ordning
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredWithActiveRentalsSortByNameDesc(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT distinct c
                        FROM Customer c
                        INNER JOIN c.rentals r
                        WHERE (c.name like :searchWord
                            OR c.email like :searchWord)
                            AND r.returnDate IS NULL
                            AND r.startDate < CURRENT_TIMESTAMP
                        ORDER BY c.name DESC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }

    /**
     * Filtrerar enligt kunder med försenade uthyrningar och ingående parametrar och sorterar enligt kundnamn i stigande ordning
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredWithLateRentalsSortByNameAsc(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT distinct c
                        FROM Customer c
                        INNER JOIN c.rentals r
                        WHERE (c.name like :searchWord
                            OR c.email like :searchWord)
                            AND r.returnDate IS NULL
                            AND r.endDate < CURRENT TIMESTAMP
                        ORDER BY c.name ASC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }

    /**
     * Filtrerar enligt kunder med försenade uthyrningar och ingående parametrar och sorterar enligt kundnamn i fallande ordning
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredWithLateRentalsSortByNameDesc(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT distinct c
                        FROM Customer c
                        INNER JOIN c.rentals r
                        WHERE (c.name like :searchWord
                            OR c.email like :searchWord)
                            AND r.returnDate IS NULL
                            AND r.endDate < CURRENT TIMESTAMP
                        ORDER BY c.name DESC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }

    /**
     * Filtrerar enligt kunder med försenade uthyrningar och ingående parametrar och sorterar enligt kund med den uthyrning som är försenad mest
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredWithLateRentals(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT c
                        FROM Customer c
                        INNER JOIN c.rentals r
                        WHERE (c.name like :searchWord
                            OR c.email like :searchWord)
                            AND r.returnDate IS NULL
                            AND r.endDate < CURRENT TIMESTAMP
                        GROUP BY c
                        ORDER BY MIN(r.endDate) ASC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }

    /**
     * Filtrerar enligt ingående parametrar och sorterar enligt namn i stigande ordning
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredSortByNameAsc(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT c
                        FROM Customer c
                        WHERE (c.name like :searchWord
                            OR c.email like :searchWord)
                        ORDER BY c.name ASC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }

    /**
     * Filtrerar enligt ingående parametrar och sorterar enligt namn i fallande ordning
     * @param searchWord Söker i namn och mail. Skriv "" för att inte filtrera enligt sökord
     * @return returnerar en lista av kunder enligt filtreringen och sorteringen
     */
    @Override
    public List<Customer> findFilteredSortByNameDesc(String searchWord) {
        try(Session session = sessionFactory.openSession()){
            String hql = """
                        SELECT c
                        FROM Customer c
                        WHERE c.name like :searchWord
                            OR c.email like :searchWord
                        ORDER BY c.name DESC
                        """;

            return session.createQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }
}
