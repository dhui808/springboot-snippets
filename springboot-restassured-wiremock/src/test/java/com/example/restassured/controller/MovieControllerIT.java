package com.example.restassured.controller;

import com.example.restassured.model.Movie;
import com.example.restassured.service.MovieService;
import io.restassured.common.mapper.TypeRef;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MovieControllerIT {

    @LocalServerPort
    private int port;

    private String uri;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    @MockitoBean
    MovieService movieService;

    @Test
    public void givenMovieId_whenMakingGetRequestToMovieEndpoint_thenReturnMovie() {

        Movie testMovie = new Movie(1, "movie1", "summary1");
        when(movieService.findMovie(1)).thenReturn(testMovie);

        get(uri + "/movie/" + testMovie.getId()).then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(testMovie.getId()))
            .body("name", equalTo(testMovie.getName()))
            .body("synopsis", notNullValue());

        Movie result = get(uri + "/movie/" + testMovie.getId()).then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(Movie.class);
        assertThat(result).isEqualTo(testMovie);

        String responseString = get(uri + "/movie/" + testMovie.getId()).then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .asString();
        assertThat(responseString).isNotEmpty();
    }

    @Test
    public void givenSetMovie_whenCallingMoviesEndpoint_thenReturnAllMovies() {

        Set<Movie> movieSet = new HashSet<>();
        movieSet.add(new Movie(1, "movie1", "summary1"));
        movieSet.add(new Movie(2, "movie2", "summary2"));
        when(movieService.getAll()).thenReturn(movieSet);

        get(uri + "/movies").then()
            .statusCode(HttpStatus.OK.value())
            .assertThat()
            .body("size()", is(2));

        Movie[] movies = get(uri + "/movies").then()
            .statusCode(200)
            .extract()
            .as(Movie[].class);
        assertThat(movies.length).isEqualTo(2);
    }
    
    @Test
    public void whenCallingMoviesEndpoint_thenReturnAllMoviesAsList() {
        Set<Movie> movieSet = new HashSet<>();
        movieSet.add(new Movie(1, "movie1", "summary1"));
        movieSet.add(new Movie(2, "movie2", "summary2"));
        when(movieService.getAll()).thenReturn(movieSet);

        List<Movie> movies = get(uri + "/movies").then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<List<Movie>>() {});

        assertThat(movies.size()).isEqualTo(2);
        assertThat(movies).usingFieldByFieldElementComparator().containsAll(movieSet);
    }

    @Test
    public void givenMovie_whenMakingPostRequestToMovieEndpoint_thenCorrect() {

        Map<String, String> request = new HashMap<>();
        request.put("id", "11");
        request.put("name", "movie1");
        request.put("synopsis", "summary1");

        int movieId = given().contentType("application/json")
            .body(request)
            .when()
            .post(uri + "/movie")
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .path("id");
        assertThat(movieId).isEqualTo(11);

    }
}
