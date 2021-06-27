package com.udacity.jdnd.course3.critter.exception;

import javax.persistence.EntityNotFoundException;

public class CustomerNotFoundException extends EntityNotFoundException {

    private final Long id;

    public CustomerNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
