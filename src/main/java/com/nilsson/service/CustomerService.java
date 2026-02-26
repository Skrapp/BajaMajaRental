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

    public Customer update(Customer customer){
        if (customer.getName() == null || customer.getName().isBlank()) throw new IllegalArgumentException("name krävs");
        if (customer.getEmail() == null || customer.getEmail().isBlank()) throw new IllegalArgumentException("email krävs");

        customerRepository.save(customer);
        return customer;
    }

    public Customer findById(Long id) {
        if(id <= 0) throw new IllegalArgumentException("ID är inte godkänt.");

        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()){
            throw new CustomerNotFoundException(id);
        }
        return customerOptional.get();
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public enum SortOrder {
        NAME_ASC,
        NAME_DESC,
        DEFAULT
    }

    public enum RequireRental{
        ANY_RENTAL,
        ACTIVE_RENTAL,
        LATE_RENTAL,
        NOT_REQUIRED
    }

    public List<Customer> findAllFiltered(String searchWord, RequireRental requireRental, SortOrder orderBy){
        if(searchWord == null || searchWord.isBlank()) searchWord = "";
        if(orderBy == null) orderBy = SortOrder.DEFAULT;
        if(requireRental == null) requireRental = RequireRental.NOT_REQUIRED;

        //Om man inte söker efter nåt speciellt så vill man ha allt
        final boolean noFiltersApplied =
                searchWord.isBlank() && requireRental == RequireRental.NOT_REQUIRED && orderBy == SortOrder.DEFAULT;
        if (noFiltersApplied) {
            return findAll();
        }

        //Skickar vidare beroende på RequireRental typ och SortOrder typ
        return switch (requireRental) {
            case ANY_RENTAL -> switch (orderBy) {
                    case NAME_DESC -> customerRepository.findFilteredWithAnyRentalsSortByNameDesc(searchWord);
                    default -> customerRepository.findFilteredWithAnyRentalsSortByNameAsc(searchWord);
            };

            case ACTIVE_RENTAL -> switch (orderBy) {
                    case NAME_DESC -> customerRepository.findFilteredWithActiveRentalsSortByNameDesc(searchWord);
                    default -> customerRepository.findFilteredWithActiveRentalsSortByNameAsc(searchWord);
            };

            case LATE_RENTAL -> switch (orderBy) {
                    case NAME_ASC -> customerRepository.findFilteredWithLateRentalsSortByNameAsc(searchWord);
                    case NAME_DESC -> customerRepository.findFilteredWithLateRentalsSortByNameDesc(searchWord);
                    default -> customerRepository.findFilteredWithLateRentals(searchWord);
            };
            case NOT_REQUIRED -> switch (orderBy){
                case NAME_DESC -> customerRepository.findFilteredSortByNameDesc(searchWord);
                default -> customerRepository.findFilteredSortByNameAsc(searchWord);
            };
        };
    }
}
