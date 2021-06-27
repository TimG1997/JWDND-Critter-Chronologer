package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.exception.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    @Transactional
    public Customer getCustomer(Long id) {
        Optional<Customer> customerOptional = this.customerRepository.findById(id);

        if (customerOptional.isPresent()) {
            return customerOptional.get();
        } else {
            throw new CustomerNotFoundException(id);
        }
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {
        return this.customerRepository.save(customer);
    }

    @Transactional
    public List<Customer> getAllCustomers() {
        return (List<Customer>) this.customerRepository.findAll();
    }

    @Transactional
    public Customer getCustomerByPet(long petId) {
        boolean petExists = this.petRepository.existsById(petId);

        if (petExists) {
            Optional<Customer> customerOptional = this.customerRepository.findByPetsId(petId);
            if (customerOptional.isPresent()) {
                return customerOptional.get();
            } else {
                throw new CustomerNotFoundException(-1L);
            }
        } else {
            throw new PetNotFoundException(petId);
        }

    }
}
