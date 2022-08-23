package com.example.conferenceapp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConferenceIntegrationTest {


    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;
    private ValidatableResponse validatableResponse;


    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }


    @Test
    public void ShouldReturnConferenceTime() {

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/conference")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("start_time", notNullValue());


    }

    @Test
    public void ShouldNotReturnConferenceWithAddedTimeCannotBeBeforeNow() {
        String time = "{\"time\":\"01/05/2021 10:00:00\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(time)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/conference")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Date cannot be before now"));


    }


    @Test
    public void ShouldNotReturnConferenceWithAddedTimeBadDate() {
        String time = "{\"time\":\"Bad date\"}";


        validatableResponse = given()
                .contentType(ContentType.JSON).body(time)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/conference")
                .then()
                .assertThat().log().all().statusCode(400);


    }


    @Test
    public void ShouldReturnConferenceTimeWithAddedTime() {
        String time = "{\"time\":\"01/09/2023 10:00:00\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(time)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/conference")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("lectures", notNullValue());


    }

    @Test
    public void ShouldReturnEditedConference() {
        String conference = """
                {
                    "start_time": "01/06/2023 10:00:00",
                    "end_time": "01/06/2023 15:45:00",
                    "themes":3,
                    "participants":5
                }""";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(conference)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/conference")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("start_time", equalTo("01/06/2023 10:00:00"))
                .body("end_time", equalTo("01/06/2023 15:45:00"));


    }

    @Test
    public void ShouldNotReturnEditedConferenceInvalidTheme() {
        String conference = """
                                {
                                "start_time": "01/06/2021 10:00:00",
                                "end_time": "01/06/2021 15:45:00",
                                "themes":-1,
                                "participants":1
                }""";


        validatableResponse = given()
                .contentType(ContentType.JSON).body(conference)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/conference")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Theme must be larger than 0"));


    }

    @Test
    public void ShouldNotReturnEditedConferenceInvalidParticipants() {
        String conference = """
                                {
                                "start_time": "01/06/2021 10:00:00",
                                "end_time": "01/06/2021 15:45:00",
                                "themes":1,
                                "participants":-1
                }""";

        validatableResponse = given()
                .contentType(ContentType.JSON).body(conference)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/conference")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid participants number"));


    }

    @Test
    public void ShouldNotReturnEditedConferenceInvalidDate() {
        String conference = """
                                {
                                "start_time": "01/06/2021 10:00:00",
                                "end_time": "01/06/2020 15:45:00",
                                "themes":1,
                                "participants":-1
                }""";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(conference)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/conference")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid date"));


    }

    @Test
    public void ShouldReturnConferenceWithDeletedTime() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .delete("/conference/1")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("lectures", notNullValue());


    }

    @Test
    public void ShouldNotReturnConferenceWithDeletedTime() {

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .delete("/conference/1231")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid lecture index"));


    }
}
