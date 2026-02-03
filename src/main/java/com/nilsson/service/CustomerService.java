package com.nilsson.service;

import com.nilsson.entity.Customer;
import com.nilsson.exception.CustomerNotFoundException;
import com.nilsson.repo.CustomerRepo;

import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final CustomerRepo customerRepository;

    public CustomerService(CustomerRepo customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(String name, String email) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name krävs");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email krävs");

        Customer customer = new Customer(name.trim(), email.trim());
        customerRepository.save(customer);
        return customer;
    }

    public Customer findById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()){
            throw new CustomerNotFoundException(id);
        }
        return customerOptional.get();
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public List<Customer> findAllFiltered(String searchWord, boolean requireRentals){
        return customerRepository.findAllFiltered(searchWord, requireRentals);
    }
}
