package com.nilsson.repo;

import com.nilsson.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo {

    void save(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    List<Customer> findAllFiltered(String searchWord, boolean requireRentals);
}
