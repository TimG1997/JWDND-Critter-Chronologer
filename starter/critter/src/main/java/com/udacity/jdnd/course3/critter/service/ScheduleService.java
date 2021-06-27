package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exception.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exception.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, PetRepository petRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    public Schedule createSchedule(Schedule schedule) {
        return this.scheduleRepository.save(schedule);
    }

    @Transactional
    public List<Schedule> getSchedules() {
        return (List<Schedule>) this.scheduleRepository.findAll();
    }

    @Transactional
    public List<Schedule> getSchedulesForEmployee(Long employeeId) {
        if(!this.employeeRepository.existsById(employeeId)){
            throw new EmployeeNotFoundException(employeeId);
        }

        List<Schedule> schedules = (List<Schedule>) this.scheduleRepository.findAll();
        return schedules.stream()
                .filter(schedule -> schedule.getEmployees()
                        .stream()
                        .map(Employee::getId)
                        .collect(Collectors.toList())
                        .contains(employeeId))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Schedule> getSchedulesForPet(long petId) {
        if(!this.petRepository.existsById(petId)){
            throw new PetNotFoundException(petId);
        }

        List<Schedule> schedules = (List<Schedule>) this.scheduleRepository.findAll();
        return schedules.stream()
                .filter(schedule -> schedule.getPets()
                        .stream()
                        .map(Pet::getId)
                        .collect(Collectors.toList())
                        .contains(petId))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Schedule> getSchedulesForCustomer(Long customerId) {
        if(this.customerRepository.existsById(customerId)){
            throw new CustomerNotFoundException(customerId);
        }

        List<Schedule> schedules = (List<Schedule>) this.scheduleRepository.findAll();
        return schedules.stream().filter(
                        schedule -> schedule.getPets().stream()
                                                .map(Pet::getOwner)
                                                .map(Customer::getId)
                                                .anyMatch(ownerId -> ownerId.equals(customerId)))
                        .collect(Collectors.toList());

    }
}
