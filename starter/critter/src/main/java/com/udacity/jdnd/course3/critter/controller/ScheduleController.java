package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.controller.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final PetService petService;
    private final EmployeeService employeeService;

    public ScheduleController(ScheduleService scheduleService, PetService petService, EmployeeService employeeService) {
        this.scheduleService = scheduleService;
        this.petService = petService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        List<Pet> pets = this.petService.getPetsForIds(scheduleDTO.getPetIds());
        List<Employee> employees = this.employeeService.getEmployees(scheduleDTO.getEmployeeIds());

        Schedule schedule = new Schedule(employees, pets, scheduleDTO.getDate(), scheduleDTO.getActivities());
        Schedule createdSchedule = this.scheduleService.createSchedule(schedule);

        scheduleDTO.setId(createdSchedule.getId());

        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = this.scheduleService.getSchedules();
        return mapToScheduleDTOs(schedules);
    }


    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = this.scheduleService.getSchedulesForPet(petId);
        return mapToScheduleDTOs(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = this.scheduleService.getSchedulesForEmployee(employeeId);
        return mapToScheduleDTOs(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = this.scheduleService.getSchedulesForCustomer(customerId);
        return mapToScheduleDTOs(schedules);
    }

    private List<ScheduleDTO> mapToScheduleDTOs(List<Schedule> schedules) {
        return schedules.stream().map(schedule -> {
            List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
            List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());

            return new ScheduleDTO(schedule.getId(), employeeIds, petIds, schedule.getLocalDate(), schedule.getActivities());
        }).collect(Collectors.toList());
    }
}
