package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getPetsForIds(List<Long> ids){
        return this.petRepository.findAllByIdIn(ids);
    }

    public Pet createPet(Pet pet){
        return this.petRepository.save(pet);
    }

    public Pet getPet(Long id){
        Optional<Pet> petOptional = this.petRepository.findById(id);
        if(petOptional.isPresent()){
            return petOptional.get();
        } else {
            throw new PetNotFoundException(id);
        }
    }

    public List<Pet> getPetsByOwner(Long ownerId){
        return this.petRepository.findAllByOwnerId(ownerId);
    }
}
