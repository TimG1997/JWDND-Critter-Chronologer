package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.controller.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.controller.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.controller.dto.EmployeeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final CustomerService customerService;
    private final PetService petService;
    private final EmployeeService employeeService;

    @Autowired
    public UserController(CustomerService customerService, PetService petService, EmployeeService employeeService) {
        this.customerService = customerService;
        this.petService = petService;
        this.employeeService = employeeService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer createdCustomer;

        if (customerDTO.getPetIds() != null) {
            List<Pet> pets = this.petService.getPetsForIds(customerDTO.getPetIds());
            Customer customer = new Customer(customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes(), pets);
            createdCustomer = this.customerService.saveCustomer(customer);
        } else {
            createdCustomer = this.customerService.saveCustomer(new Customer(customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes()));
        }

        customerDTO.setId(createdCustomer.getId());

        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        return this.customerService.getAllCustomers().stream().map(customer -> {
            List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
            return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), customer.getNotes(), petIds);
        }).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Customer customer = this.customerService.getCustomerByPet(petId);
        List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), customer.getNotes(), petIds);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getName(), employeeDTO.getSkills(), employeeDTO.getDaysAvailable());
        Employee createdEmployee = this.employeeService.saveEmployee(employee);
        employeeDTO.setId(createdEmployee.getId());
        return employeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = this.employeeService.getEmployee(employeeId);
        EmployeeDTO employeeDTO = new EmployeeDTO(employee.getId(), employee.getName(), employee.getSkills(), employee.getDaysAvailable());
        return employeeDTO;
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = this.employeeService.getEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable);
        this.employeeService.saveEmployee(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = this.employeeService.getEmployeesForService(employeeDTO.getSkills(), employeeDTO.getDate());
        return employees.stream().
                map(employee -> new EmployeeDTO(
                                                employee.getId(),
                                                employee.getName(),
                                                employee.getSkills(),
                                                employee.getDaysAvailable()))
                .collect(Collectors.toList());
    }

}
