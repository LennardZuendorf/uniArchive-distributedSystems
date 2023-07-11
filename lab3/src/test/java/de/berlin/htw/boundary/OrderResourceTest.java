package de.berlin.htw.boundary;

import de.berlin.htw.boundary.dto.Item;
import de.berlin.htw.entity.dao.UserRepository;
import de.berlin.htw.entity.dto.UserEntity;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
class OrderResourceTest {

    final String USERID = "1";
    final String ORDERSBASEPATH="/orders";
    final String BASKETBASEPATH = "/basket";
    final String PRODUCTID = "1-2-3-4-5-6";
    final String PRODUCTNAME = "TestProduct";

    @Inject
    protected RedisDataSource redisDS;

    @Inject
    UserRepository userRepository;

    @AfterEach
    void cleanup()  {
        UserEntity user = userRepository.findUserById(Integer.valueOf(USERID));
        user.setBalance(120.30F);
        userRepository.updateUser(user);
    }

    @Test
    void testUnauthorized() {
        given()
            .log().all()
            .when().get("/orders")
            .then()
            .log().all()
            .statusCode(401);
    }

    @Test
    void testCheckout() {
        Item item = new Item();
        item.setProductId(PRODUCTID);
        item.setCount(1);
        item.setPrice(20.30F);
        item.setProductName(PRODUCTNAME);

        //adding Item
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .post(BASKETBASEPATH + "/" + PRODUCTID)
                .then()
                .statusCode(201)
                .header("location", "http://localhost:8081/basket/"+PRODUCTID);


        given()
                .log().all()
                .when().header("X-User-Id", USERID)
                .post(BASKETBASEPATH)
                .then()
                .log().all()
                .statusCode(201)
                .and().header("location", startsWith("http://localhost:8081/orders/"));

        //validate balance of returned orders element
        given()
        	.log().all()
        	.when().header("X-User-Id", USERID)
        	.get(ORDERSBASEPATH)
        	.then()
        	.log().all()
        	.statusCode(200)
            .and().body("balance", Matchers.equalTo(20.30F));

        //asserting that redis is empty and user balance is correct
        assertFalse(redisDS.key().exists(USERID));
        assertEquals(100, userRepository.findUserById(Integer.valueOf(USERID)).getBalance());
    }
}