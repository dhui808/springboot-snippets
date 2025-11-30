## Use @AutoConfigureCache and @EnableCaching
    Not needed when using @SpringBootTest without attributes.

## H2 console
    http://localhost:8080/h2-console
    JDBC URL: jdbc:h2:mem:myh2db

    When the Spring Boot application starts, it automatically detects the 
    src/main/resources/data.sql file (and optionally schema.sql for table creation)
    and executes its contents against the H2 database.

## Test with curl
```bash
curl -v localhost:8080/products

curl -v http://localhost:8080/products/1

curl -v -X POST  \
  -H "Content-Type: application/json"  \
  -d '{"name":"Widget","price":9.99,"available":true}'  \
  http://localhost:8080/products

curl -v -X PUT  \
  -H "Content-Type: application/json"  \
  -d '{"name":"Widget Pro","price":19.99,"description":"Updated"}'  \
  http://localhost:8080/products/5

curl -v -X DELETE http://localhost:8080/products/1

curl -v http://localhost:8080/products/details/1

curl -v http://localhost:8080/products/price-check?productId=1

curl -v -X POST http://localhost:8080/products/clear-cache
```

## Enable SLF4J with lombok
Add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.42</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.17</version>
</dependency>
```
### Enable logback as the logging implementation
    Add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.21</version>
</dependency>
```
    Add the configuration file `logback.xml` to your `src/main/resources` directory.

## Unit testing with JUnit 5
    When you add spring-boot-starter-test to your pom.xml, JUnit and its necessary 
    components are automatically included as transitive dependencies, making a 
    separate declaration unnecessary.
    
    To run the tests, use the following command:
    ```bash
    mvn test
    ``` 
## Mockito for mocking in unit tests
    Transitive dependency included with `spring-boot-starter-test`.

## Java 21
    Ensure your `pom.xml` file is configured to use Java 21:
```xml
<properties>
    <java.version>21</java.version>
</properties>
```
    The spring-boot-starter-parent POM uses this property to configure the maven-compiler-plugin

## Spring Boot 3.5.8
    Ensure your `pom.xml` file includes the Spring Boot parent:
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.8</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```
## Spring Data JPA
    Add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
## H2 Database
    Add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```
## Spring Web
    Add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
## Lombok
    Add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
    <scope>provided</scope>
</dependency>
```
    Enable annotation processing in your IDE to use Lombok features.
## Build with Maven
    Ensure you have Maven installed and configured on your system.
    Use the following command to build your project:
    ```bash
    mvn clean install
    ```
## Run the Spring Boot Application
    Use the following command to run your Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```
## Dependnecy Injection in Tests
    To enable dependency injection in your test classes, annotate 
    them with `@SpringBootTest`. This will load the full application context for your tests.
    Use the "classes" attribute to specify configuration classes that load the applicationConetext.
    Note that CacheManager has to be injected in tests

## Note on @Import
    From Spring documentation:
```
Indicates one or more component classes to import — typically @Configuration classes.
Provides functionality equivalent to the <import/> element in Spring XML.
```
    When use @SpringBootTest with the "classes" attribute to specify 
    the configuration classes needed for your tests.
    Use @Import to import Java components which are needed for testing but the use of
    "classes" attribute may exclude the necessary Java components (my interpretation).
    One can also include the necessary Java components in the "classes" attribute instead 
    of using @Import.

## Run Integration Tests
    maven-failsafe-plugin needs to be configured in your `pom.xml` file to run integration tests.
    Use the following command to run your integration tests:
    ```bash
    mvn integration-test
    mvn verify
    ```

    Both commands will run unit tests and integration tests, but `mvn verify` will also run the
    `verify` phase, which includes additional checks and validations after the tests have run.

## Integration Testing using @SpringBootTest without attributes
    Spring Boot will do component scanning and dependency injection when you use @SpringBootTest 
    without attributes. It’s essentially like starting your application, but in a test context.

## Integration Testing using @SpringBootTest(classes = …)
```
- Application context creation
  Spring Boot will only load the specified configuration classes into the test’s 
  application context. It does not automatically look for your @SpringBootApplication 
  class anymore.
- Component scanning
  Component scanning is not triggered unless one of the classes you specify includes 
  @ComponentScan.
  * If you pass a plain @Configuration class without @ComponentScan, then only the beans 
    explicitly defined in that class will be registered.
  * If you pass your main application class (annotated with @SpringBootApplication), 
    then scanning will occur because @SpringBootApplication includes @ComponentScan.
- Dependency injection
  Injection works for whatever beans are present in the context. If scanning is active 
  (via @SpringBootApplication or explicit @ComponentScan), then all discovered beans are 
  available for injection. If not, only the beans defined in the provided configuration 
  classes are injectable.


