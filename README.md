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
