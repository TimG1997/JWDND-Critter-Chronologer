package com.udacity.jdnd.course3.critter.exception;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());

    private static final String SOMETHING_WENT_WRONG = "error.something-went-wrong";

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(EntityNotFoundException entityNotFoundException){
        if(entityNotFoundException instanceof PetNotFoundException){
            return new ApiError(HttpStatus.NOT_FOUND, String.format("Pet with id %d not found.", ((PetNotFoundException)entityNotFoundException).getId()));
        } else if(entityNotFoundException instanceof CustomerNotFoundException){
            return new ApiError(HttpStatus.NOT_FOUND, String.format("Customer with id %d not found.", ((CustomerNotFoundException)entityNotFoundException).getId()));
        } else if(entityNotFoundException instanceof EmployeeNotFoundException){
            return new ApiError(HttpStatus.NOT_FOUND, String.format("Employee with id %d not found.", ((CustomerNotFoundException)entityNotFoundException).getId()));
        } else {
            return new ApiError(HttpStatus.NOT_FOUND, "Entity not found");
        }
    }
}

