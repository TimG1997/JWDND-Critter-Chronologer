package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule createSchedule(Schedule schedule) {
        return this.scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedules() {
        return (List<Schedule>) this.scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesForEmployee(Long employeeId) {
        List<Schedule> schedules = (List<Schedule>) this.scheduleRepository.findAll();
        return schedules.stream()
                .filter(schedule -> schedule.getEmployees()
                        .stream()
                        .map(Employee::getId)
                        .collect(Collectors.toList())
                        .contains(employeeId))
                .collect(Collectors.toList());
    }

    public List<Schedule> getSchedulesForPet(long petId) {
        List<Schedule> schedules = (List<Schedule>) this.scheduleRepository.findAll();
        return schedules.stream()
                .filter(schedule -> schedule.getPets()
                        .stream()
                        .map(Pet::getId)
                        .collect(Collectors.toList())
                        .contains(petId))
                .collect(Collectors.toList());
    }

    public List<Schedule> getSchedulesForCustomer(Long customerId) {
        List<Schedule> schedules = (List<Schedule>) this.scheduleRepository.findAll();
        return schedules.stream().filter(
                        schedule -> schedule.getPets().stream()
                                                .map(Pet::getOwner)
                                                .map(Customer::getId)
                                                .anyMatch(ownerId -> ownerId.equals(customerId)))
                        .collect(Collectors.toList());

    }
}
