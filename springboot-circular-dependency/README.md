## Solutions for breaking circular dependency
[Circular Dependencies in Spring](https://www.baeldung.com/circular-dependencies-in-spring)

## Test
```
curl http://localhost:8080/products/details/1
curl http://localhost:8080/lazy/products/details/1
```