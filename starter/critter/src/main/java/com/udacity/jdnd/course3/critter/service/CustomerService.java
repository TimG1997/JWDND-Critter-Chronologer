package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.exception.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomer(Long id){
        Optional<Customer> customerOptional = this.customerRepository.findById(id);

        if(customerOptional.isPresent()){
            return customerOptional.get();
        } else {
            throw new CustomerNotFoundException(id);
        }
    }

    public Customer saveCustomer(Customer customer){
        return this.customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers(){
        return (List<Customer>) this.customerRepository.findAll();
    }

    public Customer getCustomerByPet(long petId) {
        return this.customerRepository.findByPetsId(petId);
    }
}
