package com.example.conferenceapp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {


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

    @AfterEach


    @Test
    public void ShouldReturnUsersList() {

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/users/details")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("$.size()", greaterThan(0));


    }

    @Test
    void shouldReturnUserById() {

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/users/details/1")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("username", notNullValue());

    }

    @Test
    void shouldNotReturnUsersById() {

        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/users/details/123123")
                .then()
                .assertThat().log().all().statusCode(404)
                .body("details[0]", equalTo("No users with id: 123123"));
    }

    @Test
    void shouldReturnUsersUsernamesAndEmails() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/users")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("$.size()", greaterThan(0));
    }

    @Test
    void shouldReturnAddedUser() {
        String user = "{\"username\":\"user22\",\"email\":\"user22@email.com\",\"password\":\"password1\",\"authority\":\"USER\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/users")
                .then()
                .assertThat().log().all().statusCode(201)
                .body("username", equalTo("user22"));
    }

    @Test
    void shouldNotReturnAddedUserInvalidEmail() {
        String user = "{\"id\":5,\"username\":\"usesadfr5\",\"email\":\"user5email.com\",\"password\":\"afsdfasdfd\",\"authority\":\"USER\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/users")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid email"));
    }


    @Test
    void shouldNotReturnAddedUserAlreadyExists() {
        String user = "{\"id\":5,\"username\":\"admin\",\"email\":\"admin@email.com\",\"password\":\"aasdasdasd\",\"authority\":\"USER\"}";

        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/users")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("User already exists"));
    }

    @Test
    void shouldNotReturnAddedUserInvalidCredentials() {
        String user = "{\"id\":5,\"username\":\"user5\",\"email\":\"user5@email.com\",\"password\":\"a\",\"authority\":\"USER\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .post("/users")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Invalid user details"));
    }

    @Test
    void shouldNotReturnAddedOrUpdatedUser() {
        String user = "{\"id\":5,\"username\":\"user5\",\"email\":\"user5email.com\",\"password\":\"aasdasdasd\",\"authority\":\"USER\"}";

        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/-1")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("User already exists"));
    }


    @Test
    void shouldReturnAddedOrUpdatedUser() {
        String user = "{\"username\":\"user13\",\"email\":\"user13@email.com\",\"password\":\"adasdasdasd\",\"authority\":\"USER\"}";

        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/3")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("username", equalTo("user13"));
    }

    @Test
    void shouldNotReturnAddedOrUpdatedUserInvalidCredentials() {
        String user = "{\"id\":5,\"username\":\"user5\",\"email\":\"user5@email.com\",\"password\":\"a\"}";

        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/1")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo(("Invalid user details")));
    }

    @Test
    void shouldNotReturnAddedOrUpdatedUserInvalidEmail() {
        String user = "{\"id\":5,\"username\":\"userasd5\",\"email\":\"user5email.com\",\"password\":\"afasdfsdf\"}";

        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/1")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo(("Invalid email")));
    }

    @Test
    void shouldNotReturnAddedOrUpdatedUserAlreadyExists() {
        String user = "{\"id\":5,\"username\":\"admin\",\"email\":\"admin@email.com\",\"password\":\"aasdasdasd\"}";

        validatableResponse = given()
                .contentType(ContentType.JSON).body(user)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/1")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("User already exists"));
    }

    @Test
    void shouldReturnUsersLectures() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .queryParam("username", "user1")
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/users/lectures")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("$.size()", greaterThan(0));
    }

    @Test
    void shouldNotReturnUsersLectures() {
        validatableResponse = given()
                .contentType(ContentType.JSON)
                .param("username", "notauser")
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .get("/users/lectures")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("No users with login: notauser"));
    }

    @Test
    void shouldReturnUserWithUpdatedEmail() {
        String email = "{\"old_email\":\"user7@email.com\",\"new_email\":\"email1@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(email)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/change_email")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("email", equalTo("email1@email.com"));
    }


    @Test
    void shouldNotReturnUserWithUpdatedEmailSameEmails() {
        String email = "{\"old_email\":\"email@email.com\",\"new_email\":\"email@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(email)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/change_email")
                .then()
                .assertThat().log().all().statusCode(400)
                .body("details[0]", equalTo("Emails must be different"));
    }

    @Test
    void shouldNotReturnUserWithUpdatedEmailBadEmails() {
        String email = "{\"old_email\":\"email@email.com\",\"new_email\":\"email1email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(email)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/change_email")
                .then()
                .assertThat().log().all().statusCode(404)
                .body("details[0]", equalTo("No user with email: email@email.com"));
    }


    @Test
    void shouldNotReturnUserWithUpdatedEmailNoUser() {
        String email = "{\"old_email\":\"email@emfsdafsdfsdfail.com\",\"new_email\":\"email17@email.com\"}";
        validatableResponse = given()
                .contentType(ContentType.JSON).body(email)
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .put("/users/change_email")
                .then()
                .assertThat().log().all().statusCode(404)
                .body("details[0]", equalTo("No user with email: email@emfsdafsdfsdfail.com"));
    }

}
