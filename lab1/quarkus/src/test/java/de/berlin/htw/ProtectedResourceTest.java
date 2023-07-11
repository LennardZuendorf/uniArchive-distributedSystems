package de.berlin.htw;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import javax.ws.rs.core.MediaType;

@QuarkusTest
class ProtectedResourceTest {

    @Test
    void testProtectedEndpoint() {
        given()
          .when()
          .get("/protected")
          .then()
             .statusCode(401)
             .header("X-My-Header", "Filter was executed");
    }

    @Test
    void testReminderEndpoint() {
        given()
          .when()
          .log().all()
          .contentType(MediaType.TEXT_PLAIN)
          .header("X-User-Name", "Max Mustermann")
          .header("X-User-Role", "ADMIN")
          .body("don't forget to learn")
          .get("/protected")
          .then()
          .log().all()
             .statusCode(403)
             .header("X-My-Header", "Filter was executed");
    }

}