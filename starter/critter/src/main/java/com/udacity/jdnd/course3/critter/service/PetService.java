package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public List<Pet> getPetsForIds(List<Long> ids){
        return this.petRepository.findAllByIdIn(ids);
    }

    @Transactional
    public Pet createPet(Pet pet){
        return this.petRepository.save(pet);
    }

    @Transactional
    public Pet getPet(Long id){
        Optional<Pet> petOptional = this.petRepository.findById(id);
        if(petOptional.isPresent()){
            return petOptional.get();
        } else {
            throw new PetNotFoundException(id);
        }
    }

    @Transactional
    public List<Pet> getPetsByOwner(Long ownerId){
        if(!this.petRepository.existsById(ownerId)){
            throw new CustomerNotFoundException(ownerId);
        }

        return this.petRepository.findAllByOwnerId(ownerId);
    }

    @Transactional
    public List<Pet> getPets() {
        return (List<Pet>) this.petRepository.findAll();
    }
}
