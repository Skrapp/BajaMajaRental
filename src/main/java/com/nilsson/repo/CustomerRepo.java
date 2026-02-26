package com.nilsson.repo;

import com.nilsson.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo {

    void save(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    List<Customer> findFilteredWithAnyRentalsSortByNameAsc(String searchWord);
    List<Customer> findFilteredWithAnyRentalsSortByNameDesc(String searchWord);
    List<Customer> findFilteredWithActiveRentalsSortByNameAsc(String searchWord);
    List<Customer> findFilteredWithActiveRentalsSortByNameDesc(String searchWord);
    List<Customer> findFilteredWithLateRentalsSortByNameAsc(String searchWord);
    List<Customer> findFilteredWithLateRentalsSortByNameDesc(String searchWord);
    List<Customer> findFilteredWithLateRentals(String searchWord);
    List<Customer> findFilteredSortByNameAsc(String searchWord);
    List<Customer> findFilteredSortByNameDesc(String searchWord);
}
