package com.example.caching.annotation;

import com.example.caching.SpringBootCachingApplication;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(classes={SpringBootCachingApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("local")
public @interface IntegrationTest2 {
}