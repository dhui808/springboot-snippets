## Spring Boot 4 Jakson Core 3

## Spring Boot 4 migration
[Spring Boot 4.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide#module-dependencies)

## Run Integration Tests
```
mvn verify
```

## Integration testing error
    java.lang.IllegalStateException: Cache already created.
    This happens when run mvn verify, but no erro when running each individual integration test class.
    Solution: add @AutoconfigureCache to each test class. However, there are cases where 
    @AutoconfigureCache has to be declared in a separate configuration class.
