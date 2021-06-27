package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.controller.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;
    private final CustomerService customerService;

    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @PostMapping("/{ownerId}")
    public PetDTO updatePet(@PathVariable(name="ownerId") Long ownerId, @RequestBody PetDTO petDTO){
        petDTO.setOwnerId(ownerId);
        return savePet(petDTO);
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer customer = this.customerService.getCustomer(petDTO.getOwnerId());

        Pet pet = new Pet(petDTO.getType(), petDTO.getName(), customer, petDTO.getBirthDate(), petDTO.getNotes());
        Pet createdPet = this.petService.createPet(pet);

        customer.getPets().add(createdPet);
        this.customerService.saveCustomer(customer);

        petDTO.setId(createdPet.getId());

        return petDTO;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = this.petService.getPet(petId);
        return new PetDTO(
                pet.getId(),
                pet.getType(),
                pet.getName(),
                pet.getOwner().getId(),
                pet.getBirthDate(),
                pet.getNotes());
    }

    @GetMapping
    public List<PetDTO> getPets() {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return this.petService.getPetsByOwner(ownerId).stream()
                .map(pet -> new PetDTO(
                        pet.getId(),
                        pet.getType(),
                        pet.getName(),
                        pet.getOwner().getId(),
                        pet.getBirthDate(),
                        pet.getNotes()))
                .collect(Collectors.toList());
    }
}
