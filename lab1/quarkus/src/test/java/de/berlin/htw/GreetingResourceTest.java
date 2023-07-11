package de.berlin.htw;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
            .when()
            .log().all()
            .get("/hello")
            .then()
            .log().all()
            .statusCode(200)
            .body(is("Hello RESTEasy"));
    }

}