### How does JVM run an executable jar file?
```
The JVM recognizes the .jar extension.
The JVM extracts the META-INF/MANIFEST.MF file from the JAR.
The Main-Class entry in the MANIFEST.MF file specifies the fully qualified name of the class that contains
the application's entry point.
```

### How does JVM run a Spring Boot jar file?
```
The META-INF/MANIFEST.MF file contains a Main-Class attribute, which is always org.springframework.boot.loader.JarLauncher.
JarLauncher looks for the Start-Class attribute in MANIFEST.MF file, which is the class that is annotated with
@SpringBootApplication.
The launcher then invokes the main method of this application-specific class.
```

### Custom Auoconfiguration
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
