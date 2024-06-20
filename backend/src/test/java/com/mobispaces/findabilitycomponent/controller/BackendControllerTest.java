package com.mobispaces.findabilitycomponent.controller;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mobispaces.findabilitycomponent.SpringBootVuejsApplication;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = SpringBootVuejsApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BackendControllerTest {

	@LocalServerPort
	private int port;

	@Before
    public void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
	
	@Test
	public void codelist_api_get_COUNTRY() {
		given()
			.pathParam("id", "COUNTRY")
		.when()
			.get("/api/codelist/{id}")
		.then()
		 .statusCode(HttpStatus.SC_OK);
	}
	
	@Test
	public void codelist_api_get_not_found_exception() {
		given()
			.pathParam("id", "ERROR")
		.when()
			.get("/api/codelist/{id}")
		.then()
		 .statusCode(HttpStatus.SC_NOT_FOUND);
	}
}