package com.example.restassured.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class RestTemplateHeaderModifierInterceptor
implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
    HttpRequest request, 
    byte[] body, 
    ClientHttpRequestExecution execution) throws IOException {
	  HttpHeaders headers = request.getHeaders();

      if (headers.getContentType() == null) {
          headers.add("Content-Type", "application/json");
      }
      //Otherwise, keep the existing Content-Type header, which is likely "application/xml".
      headers.add("Foo", "Bar");

	  headers.forEach((key, value) -> {
	        System.out.printf("Header '%s' = %s%n", key, value);
	    });
      ClientHttpResponse response = execution.execute(request, body);
      return response;
  }
}
