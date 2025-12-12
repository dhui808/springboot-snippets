package com.example.restassured.controller;

import com.example.restassured.model.Employee;
import com.example.restassured.model.Employees;
import com.example.restassured.model.Movie;
import com.example.restassured.service.EmployeeService;
import com.example.restassured.service.MovieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

@RestController
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<Employees> getAllEmployee() {

        Employees employees = employeeService.getAllEmployees();
        log.info("Finding all employees");
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable long id) {

        Employee employee = employeeService.getEmployee(id);
        log.info("Finding employee by id {}: {}", id, employee);
        if (employee == null) {
            return ResponseEntity.badRequest()
                .body("Invalid movie id");
        }

        return ResponseEntity.ok(employee);
    }

    @PostMapping("/employee")
    public ResponseEntity<?> getEmployee(@RequestBody Employee emp) {

        Employee employee = employeeService.addEmployee(emp);
        log.info("Adding employee: {}", emp);
        if (employee == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid movie id");
        }

        // Build the URI to the newly created resource (e.g., /resources/1)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/employee")
                .buildAndExpand(employee.getId())
                .toUri();

        return ResponseEntity.created(location).body(employee);
    }
}
