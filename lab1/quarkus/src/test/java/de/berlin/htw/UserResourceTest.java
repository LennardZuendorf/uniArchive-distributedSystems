package de.berlin.htw;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class UserResourceTest {

    @Test
    void testUserEndpoint() {
        given()
            .when().get("/users/88")
            .then()
            .statusCode(404);
    }

    @Test
    void testOrderEndpoint() {
        given()
            .log().all()
            .when().get("/users/88/orders")
            .then()
            .log().all()
            .statusCode(200)
            .header("X-My-Header", "Filter was executed");
    }

}