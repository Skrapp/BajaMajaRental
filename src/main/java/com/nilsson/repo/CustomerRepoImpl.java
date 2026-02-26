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
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
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
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
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
     * Retrieves a list of customers who have late rentals, filtered by the specified search word
     * and sorted by customer name in ascending order. A customer is considered to have late
     * rentals if there are rentals with no return date and an end date earlier than the current timestamp.
     *
     * @param searchWord the search term to filter customers by their name or email. Use an empty string ("")
     *                   to retrieve all customers with late rentals without applying a name or email filter.
     * @return a list of customers matching the filtering and sorting criteria
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
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
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
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
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

    /**
     *
     * @param searchWord att söka efter i namn eller mail, skriv "" för att inte söka efter något specifikt
     * @param requireRentals ange true för att få kunder som har minst en uthyrning dokumenterad
     * @return En filtrerad lista med kunder
     */
    @Override
    public List<Customer> findFiltered(String searchWord, boolean requireRentals){
        try(Session session = sessionFactory.openSession()){
            String hql;
            if(requireRentals) {
                hql = """
                        SELECT c.*
                        FROM customers c
                            INNER JOIN rentals r ON c.id = r.customer_id
                        WHERE c.name like :searchWord
                            OR c.email like :searchWord
                        GROUP BY c.id
                        """;
            }else {
                hql = """
                        SELECT c.*
                        FROM customers c
                        WHERE c.name like :searchWord
                            OR c.email like :searchWord
                        """;
            }

            return session.createNativeQuery(hql,Customer.class)
                    .setParameter("searchWord", '%'+searchWord+'%')
                    .getResultList();
        }
    }
}
