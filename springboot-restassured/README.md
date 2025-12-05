## Spring Boot 3.5.8 + RestAssured 5.5.0 + WireMock 3.13.2 Example
    Note: Spring Boot 4 has problem with Junit 5.

## TestAssured runs with a dynamic port
    When starting a restassured test, restassured starts the application on a random port.
    The real application can be running at the same time on another port (fixed as specified in application properties).

## RestAssured vs Spring MockMvc
    Both can used for unit/integration testing of Spring Boot applications.
    RestAssured can be used for unit testing controllers only.

    RestAssured is integrated with Hamcrest. Can work with Mockito + AssertJ
    Spring MockMvc can work with Mockito + JUnit. MockMvcTester is integrated with AssertJ.

    Both RestAssured and MockMvc can work with WireMock for mocking external HTTP services.

## RestAssured and Hamcrest
    REST Assured sends requests, and Hamcrest provides rich, expressive "matcher" objects 
    (like equalTo, hasItems) to easily validate JSON/XML responses, headers, status codes, 
    and response times.
