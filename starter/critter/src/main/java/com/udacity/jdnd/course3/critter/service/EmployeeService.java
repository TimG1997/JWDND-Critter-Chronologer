package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.exception.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee){
        return this.employeeRepository.save(employee);
    }

    public List<Employee> getEmployees(List<Long> employeeIds){
        return (List<Employee>) this.employeeRepository.findAllById(employeeIds);
    }

    public Employee getEmployee(Long id){
        Optional<Employee> employeeOptional = this.employeeRepository.findById(id);

        if(employeeOptional.isPresent()){
            return employeeOptional.get();
        } else {
            throw new EmployeeNotFoundException(id);
        }
    }

    public List<Employee> getEmployeesForService(Set<EmployeeSkill> skills, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<Employee> allByDaysAvailable = this.employeeRepository.findAllByDaysAvailable(dayOfWeek);
        return allByDaysAvailable.stream().filter(employee -> employee.getSkills().containsAll(skills)).collect(Collectors.toList());
    }
}
