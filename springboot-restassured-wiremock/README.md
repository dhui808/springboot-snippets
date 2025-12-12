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

## Add spring-boot-restclient with Spring Boot 3.5.8
```
java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@2e32fc22 testClass = com.example.restassured.controller.MovieControllerIT, locations = [], classes = [com.example.restassured.SpringBootRestAssuredApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true", "server.port=0"], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2d96543c, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@68be8808, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@628c4ac0, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@1ecee32c, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@4b41e4dd, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@1338fb5, org.springframework.test.context.bean.override.BeanOverrideContextCustomizer@fcb45441, org.springframework.test.context.support.DynamicPropertiesContextCustomizer@0, org.springframework.boot.test.context.SpringBootTestAnnotation@4c76f7b], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]
Caused by: java.lang.IllegalStateException: Error processing condition on org.springframework.boot.restclient.autoconfigure.RestClientAutoConfiguration
Caused by: java.lang.ClassNotFoundException: org.springframework.boot.thread.Threading
```

## Add spring-boot-restclient with Spring Boot 4.0.0
```
java.lang.IllegalStateException: Cannot serialize object because no JSON serializer found in classpath. Please put Jackson (Databind), Gson, Johnzon, or Yasson in the classpath.
```

## Use RestTemplate for Spring Boot 3.5.8
    Dependency:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
</dependency>
```
## Configure RestTemplate with Interceptors
    Intercept requests and responses to add custom headers, logging, authentication, etc.

## Use Jakatar XML Binding API with Spring Boot 3.5.8
    Dependency:
```xml
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
</dependency>
```
## Use @ConfigurationProperties 
    Use @ConfigurationProperties for making multiple environment properties available 
    to a class that uses Spring RestTemplate.
    A. Define the properties in your configuration file
    B. Create a POJO class with @Configuration and @ConfigurationProperties annotations, holding the properties.
    C. Inject the properties bean into the API client class.

## WireMock does not simulate external servers.
    When using WireMock in tests, ensure that the external service URLs in your application
    point to the WireMock server (localhost and the WireMock port). This can be achieved by using Spring profiles.

## Error happens when dependency jackson-dataformat-xml is added
```
java.lang.IllegalStateException: Expected response body to be verified as JSON, HTML or XML but no content-type was defined in the response.
Looks like xml element first-name is not mapped to Java object property fifor rstName.

Have to use JAXBConverter.
```

## Integration Test
```
Include Failsafe plugin, set integration test classes with *IT suffix.
pluginmanagement is used in parent pom to manage plugin versions. In this pom.xml, should not use pluginmanagement. Otherwise, the plugin will not be executed.
If Failsafe plugin is not included, test classes with *IT suffix will not be executed. Have to use *IntegrationTest suffix (to be executed by Surefire plugin?)

mvn integration-test
mvn verify
```