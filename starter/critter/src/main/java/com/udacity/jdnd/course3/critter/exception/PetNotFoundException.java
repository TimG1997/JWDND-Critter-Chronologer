package com.udacity.jdnd.course3.critter.exception;

import javax.persistence.EntityNotFoundException;

public class PetNotFoundException extends EntityNotFoundException {
    
    private final Long id;

    public PetNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
