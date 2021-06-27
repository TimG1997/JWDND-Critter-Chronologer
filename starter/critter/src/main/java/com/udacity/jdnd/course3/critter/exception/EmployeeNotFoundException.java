package com.udacity.jdnd.course3.critter.exception;

import javax.persistence.EntityNotFoundException;

public class EmployeeNotFoundException extends EntityNotFoundException {

    private final Long id;

    public EmployeeNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
