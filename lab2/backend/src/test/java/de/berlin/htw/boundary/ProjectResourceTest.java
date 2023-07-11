package de.berlin.htw.boundary;

import de.berlin.htw.AbstractTest;
import de.berlin.htw.lib.dto.ProjectJson;
import de.berlin.htw.lib.dto.UserJson;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import java.util.UUID;

import static io.restassured.RestAssured.given;

/**
 * @author Lennard Zündorf [s0568383@htw-berlin.de]
 * mostly copied from UserResourceTest.java by Alexander Stanik
 */
@QuarkusTest
class ProjectResourceTest extends AbstractTest {

    static final String PROJECT_BASE_PATH = "/api/v2/projects";
    static final String USER_BASE_PATH = "/api/v2/users";
    static final String PROJECTNAME = "Test Project";
    static final String PROJECTNAME2 = "Test Project 2";
    static final String USERNAME = "Max Mustermann";
    static final String EMAIL = "max.mustermann@example.org";

    static {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    void testNotFound() {
        given()
                .when().get(PROJECT_BASE_PATH + "/" + UUID.randomUUID())
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void testInvalidProject() {
        given()
                .when().get(PROJECT_BASE_PATH + "/anyId")
                .then()
                .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void testCreateAndGet() {
        final ProjectJson project = new ProjectJson();
        project.setName(PROJECTNAME);
        final Response res = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .post(PROJECT_BASE_PATH)
                .thenReturn();

        res.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/projects"));

        final String projectResourceUrl = res.header("location");

        given()
                .when().get(projectResourceUrl)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .and().body("project_name", Matchers.equalTo(PROJECTNAME));
    }

    @Test
    void testCreateAndGetAll(){
        final ProjectJson project = new ProjectJson();
        project.setName(PROJECTNAME);
        final Response res = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .post(PROJECT_BASE_PATH)
                .thenReturn();

        res.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/projects"));

        final ProjectJson project2 = new ProjectJson();
        project2.setName(PROJECTNAME2);

        final Response res2 = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project2)
                .post(PROJECT_BASE_PATH)
                .thenReturn();

        res2.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/projects"));


        given()
                .when().get(PROJECT_BASE_PATH)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .and().body("size()", Matchers.equalTo(2));
    }

    @Test
    void testCreateAndDelete() {
        final ProjectJson project = new ProjectJson();
        project.setName(PROJECTNAME);
        final Response res = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .post(PROJECT_BASE_PATH)
                .thenReturn();

        res.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/projects"));

        final String projectResourceUrl = res.header("location");

        given()
                .when().delete(projectResourceUrl)
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());

        given()
                .when().get(projectResourceUrl)
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void testCreateAndUpdate() {
        final ProjectJson project = new ProjectJson();
        project.setName(PROJECTNAME);
        final Response res = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .post(PROJECT_BASE_PATH)
                .thenReturn();

        res.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/projects"));

        final String projectResourceUrl = res.header("location");

        project.setName(PROJECTNAME2);

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .patch(projectResourceUrl)
                .then()
                .statusCode(Status.OK.getStatusCode());

        given()
                .when().get(projectResourceUrl)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .and().body("project_name", Matchers.equalTo(PROJECTNAME2));
    }

    @Test
    void testCreateAndLink(){
        //creating new project
        final ProjectJson project = new ProjectJson();
        project.setName(PROJECTNAME);
        final Response projectRes = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .post(PROJECT_BASE_PATH)
                .thenReturn();

        projectRes.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/projects"));

        final String projectResourceUrl = projectRes.header("location");
        final String projectId = projectResourceUrl.substring(projectResourceUrl.lastIndexOf("/") + 1);

        //creating new user
        final UserJson user = new UserJson();
        user.setName(USERNAME);
        user.setEmail(EMAIL);
        final Response userRes = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .post(USER_BASE_PATH)
                .thenReturn();

        userRes.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/users"));

        final String userResourceUrl = userRes.header("location");
        final String userId = userResourceUrl.substring(userResourceUrl.lastIndexOf("/") + 1);

        //linking user to project
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .patch(projectResourceUrl+"/users/"+userId)
                .then()
                .statusCode(Status.OK.getStatusCode());

        //testing if user is linked to project
        given()
                .when().get(projectResourceUrl)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .and().body("project_name", Matchers.equalTo(PROJECTNAME))
                .and().body("users.size()", Matchers.equalTo(1));
    }

    @Test
    void testCreateAndUnlink(){
        //creating new project
        final ProjectJson project = new ProjectJson();
        project.setName(PROJECTNAME);
        final Response projectRes = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .post(PROJECT_BASE_PATH)
                .thenReturn();

        projectRes.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/projects"));

        final String projectResourceUrl = projectRes.header("location");
        final String projectId = projectResourceUrl.substring(projectResourceUrl.lastIndexOf("/") + 1);

        //creating new user
        final UserJson user = new UserJson();
        user.setName(USERNAME);
        user.setEmail(EMAIL);
        final Response userRes = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .post(USER_BASE_PATH)
                .thenReturn();

        userRes.then()
                .statusCode(Status.CREATED.getStatusCode())
                .header("location", Matchers.startsWith("http://localhost:8081/api/v2/users"));

        final String userResourceUrl = userRes.header("location");
        final String userId = userResourceUrl.substring(userResourceUrl.lastIndexOf("/") + 1);

        //linking user to project
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .patch(projectResourceUrl+"/users/"+userId)
                .then()
                .statusCode(Status.OK.getStatusCode());

        //unlinking user from project
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .delete(projectResourceUrl+"/users/"+userId)
                .then()
                .statusCode(Status.OK.getStatusCode());

        //testing if user is linked to project
        given()
                .when().get(projectResourceUrl)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .and().body("project_name", Matchers.equalTo(PROJECTNAME))
                .and().body("users.size()", Matchers.equalTo(0));
    }
}