## Spring Mock Mvc can be used for integration testing without mocking

## How to check what auto-configuration is enabled
```
Enable Spring Boot debugging to see what auto-configuration is applied while running your application/tests:
debug=true

Spring Boot’s auto-configuration system works well for applications but can sometimes be a little too much for tests.
None of Spring Boot's auto-configuration is enabled by default. Therefore, you must explicitly enable 
what you want, e.g. 
@AutoConfigureCache
or, you can enable Spring Boot's entire auto-configuration with 
@EnableAutoConfiguration

My note:
When using @SpringBootTest in test class, no need to use @AutoConfigureCache. When to use @AutoConfigureCache? 
 
```
[Spring Configuration for tests](https://stackoverflow.com/questions/74766894/is-an-explicit-cachemanager-bean-definition-mandatory-when-using-spring-boot-s)  

## How does JVM run an executable jar file?
```
The JVM recognizes the .jar extension.
The JVM extracts the META-INF/MANIFEST.MF file from the JAR.
The Main-Class entry in the MANIFEST.MF file specifies the fully qualified name of the class that contains
the application's entry point.
```

## How does JVM run a Spring Boot jar file?
```
The META-INF/MANIFEST.MF file contains a Main-Class attribute, which is always org.springframework.boot.loader.JarLauncher.
JarLauncher looks for the Start-Class attribute in MANIFEST.MF file, which is the class that is annotated with
@SpringBootApplication.
The launcher then invokes the main method of this application-specific class.
```

## Custom Auoconfiguration
```
1. Create the Auto-Configuration Class: MyServiceAutoConfiguration
2. Create a spring.factories file (for Spring Boot 2.x and earlier):
src/main/resources/META-INF/spring.factories
e.g.
# src/main/resources/META-INF/spring.factories
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.example.autoconfig.MyServiceAutoConfiguration
3. Create org.springframework.boot.autoconfigure.AutoConfiguration.imports file (for Spring Boot 3.x and later):
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
e.g.
# src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.example.autoconfig.MyServiceAutoConfiguration

```
