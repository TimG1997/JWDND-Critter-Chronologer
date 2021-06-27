package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {
    List<Pet> findAllByIdIn(List<Long> ids);
}
