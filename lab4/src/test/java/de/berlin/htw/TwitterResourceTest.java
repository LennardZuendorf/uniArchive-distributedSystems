package de.berlin.htw;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TwitterResourceTest {

    @Test
    void testTweets() {
        given()
            .when()
            .log().all()
            .get("/tweets")
            .then()
            .log().all()
            .statusCode(200);
    }
}