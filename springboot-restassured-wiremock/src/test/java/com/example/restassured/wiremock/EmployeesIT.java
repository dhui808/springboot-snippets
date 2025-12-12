package com.example.restassured.wiremock;

import com.example.restassured.Util;
import com.example.restassured.model.Employee;
import com.github.tomakehurst.wiremock.WireMockServer;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class EmployeesIT {

    @LocalServerPort
    private int port;

    private String uri;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    private static WireMockServer wireMockServer;

    private static final String APPLICATION_XML = "application/xml";
    private static final String EMPLOYEES = getEmployeesXml();
    private static final String EMPLOYEE = getEmployeeXml();
    private static final String EMPLOYEE3 = getEmployee3Xml();

    @BeforeAll
    public static void before() {
        System.out.println("Setting up!");
        final int port = 8081;
        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
        configureFor("localhost", port);
    }

    @Test
    public void testGetAllEmployees() {
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/employees"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", APPLICATION_XML)
                        .withBody(EMPLOYEES)));

        get(uri + "/employees").then()
          .assertThat()
          .body("employees.size()", is(2))
          .body("employees[0].id", equalTo("1"))
          .body("employees[0].firstName", equalTo("Jane"))
          .body("employees[0].lastName", equalTo("Doe"));
    }

    @Test
    public void testGetEmployeeById() {
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/employee/1"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", APPLICATION_XML)
                        .withBody(EMPLOYEE)));
        get(uri + "/employee/1").then()
                .assertThat()
                .body("id", equalTo("1"))
                .body("firstName", equalTo("Jane"))
                .body("lastName", equalTo("Doe"));
    }


    @Test
    public void testAddEmployee() {
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo("/employee"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", APPLICATION_XML)
                        .withHeader("Accept", APPLICATION_XML)
                        .withBody(EMPLOYEE3)));

        Map<String, String> request = new HashMap<>();
        request.put("firstName", "Jack");
        request.put("lastName", "Lee");
        request.put("sex", "m");

        Employee emp = given().contentType("application/json")
                .body(request)
                .when()
                .post(uri + "/employee")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Employee.class);

        assertThat(emp.getId()).isEqualTo("3");
    }

    private static String getEmployeesXml() {
        String xml = Util.inputStreamToString(EmployeesIT.class.getResourceAsStream("/employees.xml"));
        return xml;
    }

    private static String getEmployeeXml() {
        String xml = Util.inputStreamToString(EmployeesIT.class.getResourceAsStream("/employee.xml"));
        return xml;
    }

    private static String getEmployee3Xml() {
        String xml = Util.inputStreamToString(EmployeesIT.class.getResourceAsStream("/employee3.xml"));
        return xml;
    }

    @AfterAll
    public static void after() {
        System.out.println("Running: tearDown");
        wireMockServer.stop();
    }
}
