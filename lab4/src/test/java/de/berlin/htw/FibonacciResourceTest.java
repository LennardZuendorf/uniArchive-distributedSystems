package de.berlin.htw;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

@QuarkusTest
class FibonacciResourceTest {

    @Test
    void testFibonacciSequence() {
        Map<String, Integer> input = new HashMap<>();
        input.put("last", 1);
        input.put("current", 2);
        
        given()
            .contentType(ContentType.JSON)
            .body(input)
            .when()
            .log().all()
            .get("/fibonacci")
            .then()
            .log().all()
            .statusCode(200)
            .and().body("$", Matchers.hasItem(1))
            .and().body("$", Matchers.hasItem(2))
            .and().body("$", Matchers.hasItem(3))
            .and().body("$", Matchers.hasItem(5))
            .and().body("$", Matchers.hasItem(8))
            .and().body("$", Matchers.hasItem(13))
            .and().body("$", Matchers.hasItem(21))
            .and().body("$", Matchers.hasItem(34))
            .and().body("$", Matchers.hasItem(55))
            .and().body("$", Matchers.hasItem(89));
    }

}