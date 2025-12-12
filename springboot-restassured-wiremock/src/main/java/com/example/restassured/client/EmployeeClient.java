package com.example.restassured.client;

import com.example.restassured.config.EmployeeApiProperties;
import com.example.restassured.model.Employee;
import com.example.restassured.model.Employees;
//import com.example.restassured.utils.JacksonXmlConverter;
import com.example.restassured.utils.JaxbConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeClient extends AbstractClient {

    private final EmployeeApiProperties employeeApiProperties;
    private final String baseUrl;

    public EmployeeClient(EmployeeApiProperties employeeApiProperties) {
        this.employeeApiProperties = employeeApiProperties;
        baseUrl = employeeApiProperties.getBaseUrl();
    }

    public Employees getEmployees() {
        Employees employees = restTemplate.getForObject(baseUrl + "/employees", Employees.class);
        if (employees == null || employees.getEmployees() == null || employees.getEmployees().isEmpty()) {
            return null;
        }

        return employees;
    }

    public Employee getEmployee(long id) {
        Employee employee = restTemplate.getForObject(baseUrl + "/employee/" + id, Employee.class);

        return employee;
    }

    public Employee addEmployee(Employee employee) {

        // 3. Set up request headers

        // 4. Create the request body (a POJO will be converted to XML)
        String xmlBody = null;

        try {
            xmlBody = JaxbConverter.convertToXml(employee);
            // alternatively - causing problems:
            // xmlBody = JacksonXmlConverter.convertToXml(employee);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 5. Wrap the body and headers in an HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");
        HttpEntity<String> requestEntity = new HttpEntity<>(xmlBody, headers);
        // In contrast, here is how to send a JSON body:
        // HttpEntity<Employee> requestEntity = new HttpEntity<>(employee, headers);

        // 6. Send the POST request using postForEntity()
        // Parameters: URL, request entity, expected response body type
        ResponseEntity<Employee> responseEntity = restTemplate.postForEntity(
                baseUrl + "/employee",
                requestEntity,
                Employee.class // The class to map the response body to
        );

        // 7. Process the response
        Employee emp = null;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            emp = responseEntity.getBody();
            log.info("Added employee: {}", emp);
        } else {
            System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
        }

        return emp;
    }
}

