package com.example.restassured.service;

import com.example.restassured.client.EmployeeClient;
import com.example.restassured.model.Employee;
import com.example.restassured.model.Employees;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService {

    private EmployeeClient employeeClient;

    public EmployeeService(EmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

//    public Employee getEmployee(long id) {
//
//        Employees employees = employeeClient.getEmployees();
//        log.info("Fetched employee: {}", employees);
//
//        Employee employee = employees.getEmployees()
//                .stream()
//                .filter(emp -> Long.parseLong(emp.getId()) == id)
//                .findFirst()
//                .orElse(null);
//        return employee;
//    }

    public Employees getAllEmployees() {

        Employees employees = employeeClient.getEmployees();
        log.info("Fetched employees:", employees);

        return employees;
    }

    public Employee getEmployee(long id) {

        Employee employee = employeeClient.getEmployee(id);
        log.info("Fetched employee:", employee);

        return employee;
    }

    public Employee addEmployee(Employee employee) {

        Employee emp = employeeClient.addEmployee(employee);
        log.info("Added employee:", emp);

        return emp;
    }
}
