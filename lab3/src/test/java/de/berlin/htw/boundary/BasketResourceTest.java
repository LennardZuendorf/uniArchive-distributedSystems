package de.berlin.htw.boundary;

import de.berlin.htw.boundary.dto.Item;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class BasketResourceTest {

    final String BASKETBASEPATH = "/basket";
    final String USERID = "1";
    final String USERID2 = "2";
    final String PRODUCTID = "1-2-3-4-5-6";
    final String PRODUCTID2 = "6-5-4-3-2-1";
    final String WRONGPRODUCTID = "1-2-3-4-5-6-7-8-9-10-11-12-13-14-15-16";
    final String PRODUCTNAME = "TestProduct";
    final String PRODUCTNAME2 = "TestProduct2";

    @Inject
    protected RedisDataSource redisDS;

    @AfterEach
    void cleanup()  {
        if(redisDS.key().exists(USERID)) {
            redisDS.key().del(USERID);
        }

        if(redisDS.key().exists(PRODUCTID)) {
            throw new RuntimeException("ProductID should not exist anymore");
        }
    }

    @Test
    void testArticleNameConvention() {

        Item item = new Item();
        item.setProductName(RandomStringUtils.random(300, true, true));
        item.setProductId(PRODUCTID);
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .post(BASKETBASEPATH + "/"+PRODUCTID)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    void testWrongProductID() {

        Item item = new Item();
        item.setProductName(PRODUCTNAME);
        item.setProductId(WRONGPRODUCTID);
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .post(BASKETBASEPATH + "/"+WRONGPRODUCTID)
                .then()
                .log().all()
                .statusCode(400);
    }


    @Test
    void testPriceTooLow() {

        Item item = new Item();
        item.setPrice(2F);
        item.setProductId(PRODUCTID);
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .post(BASKETBASEPATH + "/"+PRODUCTID)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    void testPriceTooHigh() {

        Item item = new Item();
        item.setPrice(500F);
        item.setProductId(PRODUCTID);
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .post(BASKETBASEPATH + "/" + PRODUCTID)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    void testCount() {

        Item item = new Item();
        item.setProductId(PRODUCTID);
        item.setCount(11);
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .post(BASKETBASEPATH + "/"+PRODUCTID)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    void testAddItem(){
        Item item = new Item();
        item.setProductId(PRODUCTID);
        item.setCount(1);
        item.setPrice(10F);
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

        //asserting that the item is in the basket
        given()
                .when()
                .header("X-User-Id", USERID)
                .get(BASKETBASEPATH)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and().body("items[0].productId", Matchers.equalTo(PRODUCTID));

        //asserting that the ttl is set
        assertTrue(redisDS.key().ttl(USERID) < 121);

    }

    @Test
    void testUpdateItem(){
        Item item = new Item();
        item.setProductId(PRODUCTID);
        item.setCount(1);
        item.setPrice(10F);
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

        //updating Item
        item.setCount(2);

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .patch(BASKETBASEPATH + "/" + PRODUCTID)
                .then()
                .statusCode(200);

        //asserting that the item count was updated
        given()
                .when()
                .header("X-User-Id", USERID)
                .get(BASKETBASEPATH)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and().body("items[0].count", Matchers.equalTo(2));

        //asserting that the ttl is set
        assertTrue(redisDS.key().ttl(USERID) < 121);

    }

    @Test
    void testDeleteItem(){
        Item item = new Item();
        item.setProductId(PRODUCTID);
        item.setCount(1);
        item.setPrice(10F);
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

        //updating Item
        item.setProductId(PRODUCTID2);
        item.setProductName(PRODUCTNAME2);

        //adding second Item
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .post(BASKETBASEPATH + "/" + PRODUCTID2)
                .then()
                .statusCode(201)
                .header("location", "http://localhost:8081/basket/"+PRODUCTID2);

        //asserting that the items are in the basket
        given()
                .when().header("X-User-Id", USERID)
                .get(BASKETBASEPATH)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and().body("items", Matchers.hasSize(2));


        //deleting first Item
        given()
                .when().header("X-User-Id", USERID)
                .delete(BASKETBASEPATH + "/" + PRODUCTID)
                .then()
                .statusCode(200);

        //asserting that the item is not the basket
        given()
                .when().header("X-User-Id", USERID)
                .get(BASKETBASEPATH)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and().body("items", Matchers.hasSize(1));

        //asserting that the ttl is set
        assertTrue(redisDS.key().ttl(USERID) < 121);
    }

    @Test
    void testEmptyBasket(){
        Item item = new Item();
        item.setProductId(PRODUCTID);
        item.setCount(1);
        item.setPrice(10F);
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

        //updating Item
        item.setProductId(PRODUCTID2);
        item.setProductName(PRODUCTNAME2);

        //adding second Item
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID)
                .body(item)
                .post(BASKETBASEPATH + "/" + PRODUCTID2)
                .then()
                .statusCode(201)
                .header("location", "http://localhost:8081/basket/"+PRODUCTID2);

        //asserting that the items are in the basket
        given()
                .when().header("X-User-Id", USERID)
                .get(BASKETBASEPATH)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and().body("items", Matchers.hasSize(2));

        //emptying the basket
        given()
                .when().header("X-User-Id", USERID)
                .delete(BASKETBASEPATH)
                .then()
                .statusCode(204);

        //asserting that the basket is emptied and got deleted
        given()
                .when().header("X-User-Id", USERID)
                .get(BASKETBASEPATH)
                .then()
                .statusCode(404);

        //asserting that redis is empty
        assertFalse(redisDS.key().exists(USERID));
    }

    @Test
    void testBalanceValidation() {
        //creating Item
        Item item = new Item();
        item.setProductId(PRODUCTID);
        item.setCount(1);
        item.setPrice(100F);
        item.setProductName(PRODUCTNAME);

        //adding Item
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .when().header("X-User-Id", USERID2)
                .body(item)
                .post(BASKETBASEPATH + "/" + PRODUCTID)
                .then()
                .statusCode(402);

        //asserting that redis is empty
        assertFalse(redisDS.key().exists(USERID2));
    }
}
