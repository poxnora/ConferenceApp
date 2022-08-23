package com.example.conferenceapp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LectureIntegrationTest {


    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    private ValidatableResponse validatableResponse;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }


    @Test
    public void ShouldReturnLectureList() {

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/lectures")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("$.size()", greaterThan(0));


    }

    @Test
    void shouldReturnLecturesById() {

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/lectures/1")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("theme", notNullValue())
                .body("title", notNullValue());
    }

    @Test
    void shouldNotReturnLecturesById() {

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/lectures/123123")
                .then()
                .assertThat().log().all().statusCode(404)
                .body("details[0]", equalTo("No lectures with id: 123123"));
    }

    @Test
    void shouldReturnLecturesTitles() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/lectures/titles")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("[0].starts", notNullValue());
    }

    @Test
    void shouldReturnLecturesPopularity() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/lectures/popularity")
                .then()
                .assertThat().log().all().statusCode(200);
    }

    @Test
    void shouldReturnLecturesThemePopularity() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/lectures/theme_popularity")
                .then()
                .assertThat().log().all().statusCode(200);

    }

    @Test
    void shouldNotReturnAddedLectureInvalidTheme() {
        String lecture = "{\"id\":1,\"title\":\"l2\",\"theme\":-1,\"starts\":2}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(lecture)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/lectures")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid theme"));
    }

    @Test
    void shouldReturnAddedLecture() {
        String lecture = "{\"id\":1,\"title\":\"l2\",\"theme\":1,\"starts\":2}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(lecture)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/lectures")
                .then()
                .assertThat().log().all().statusCode(201)
                .body("title", equalTo("l2"));
    }

    @Test
    void shouldReturnAddedOrUpdatedLecture() {
        String lecture = "{\"id\":1,\"title\":\"l2\",\"theme\":2,\"starts\":2}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(lecture)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/1")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("title", equalTo("l2"));
    }

    @Test
    void shouldNotReturnAddedOrUpdatedLectureInvalidTheme() {
        String lecture = "{\"id\":1,\"title\":\"l2\",\"theme\":-1,\"starts\":2}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(lecture)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/1")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid theme"));
    }

    @Test
    void shouldNotReturnAddedOrUpdatedLecture() {
        String lecture = "{\"id\":1,\"title\":\"l2\",\"theme\":1,\"starts\":2}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(lecture)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/err")
                .then()
                .assertThat().log().all().statusCode(400);

    }

    @Test
    void shouldReturnLectureWithAddedUser() {
        String user = "{\"id\":5,\"username\":\"user5\",\"email\":\"user5@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/9/users")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("participants.size()", equalTo(4));
    }

    @Test
    void shouldNotReturnLectureWithAddedUserInvalidCredentials1() {
        String user = "{\"id\":5,\"username\":\"user5\",\"email\":\"user5email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/5/users")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid login or email"));
    }

    @Test
    void shouldNotReturnLectureWithAddedUserInvalidCredentials2() {
        String user = "{\"id\":5,\"username\":\"NoUsername\",\"email\":\"user5@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/9/users")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid login or email"));
    }

    @Test
    void shouldNotReturnLectureWithAddedUser() {
        String user = "{\"username\":\"NoUsername\",\"email\":\"user5@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/123123/users")
                .then()
                .assertThat().log().all().statusCode(404)
                .body("details[0]", equalTo("No lecture with id: 123123"));
    }

    @Test
    void shouldReturnLectureWithCanceledUser() {
        String user = "{\"username\":\"user1\",\"email\":\"user1@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/1/users/cancellation")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("participants[0].username", not("user1"));
    }


    @Test
    void shouldNotReturnLectureWithCanceledUserNotJoin() {
        String user = "{\"username\":\"admin\",\"email\":\"admin@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/1/users/cancellation")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("User with login: admin didn't join lecture with id: 1"));
    }

    @Test
    void shouldNotReturnLectureWithCanceledUserInvalidCredentials1() {
        String user = "{\"username\":\"admin\",\"email\":\"adminemail.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/1/users/cancellation")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid login or email"));
    }

    @Test
    void shouldNotReturnLectureWithCanceledUserInvalidCredentials2() {
        String user = "{\"username\":\"Invalid\",\"email\":\"admin@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/1/users/cancellation")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid login or email"));
    }

    @Test
    void shouldNotReturnLectureWithCanceledUser() {
        String user = "{\"username\":\"Invalid\",\"email\":\"admin@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/19789/users/cancellation")
                .then()
                .assertThat().log().all().statusCode(404)
                .body("details[0]", equalTo("No lecture with id: 19789"));
    }

    @Test
    void shouldNotReturnLectureWithCanceledUserDidntJoin() {
        String user = "{\"username\":\"admin\",\"email\":\"admin@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/lectures/1/users/cancellation")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("User with login: admin didn't join lecture with id: 1"));
    }

    @Test
    void shouldReturnLecturePopularity() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/lectures/popularity")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("[0].Lecture", notNullValue());

    }


    @Test
    void shouldReturnLectureThemePopularity() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/lectures/theme_popularity")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("[0].Popularity", notNullValue());
    }
}
