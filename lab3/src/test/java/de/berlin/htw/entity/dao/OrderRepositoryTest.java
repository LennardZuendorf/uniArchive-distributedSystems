package de.berlin.htw.entity.dao;

import de.berlin.htw.boundary.dto.Item;
import de.berlin.htw.AbstractTest;
import de.berlin.htw.entity.dto.PurchaseEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.transaction.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class OrderRepositoryTest extends AbstractTest {

    final String USERID = "1";
    final String PRODUCTID = "1-2-3-4-5-6";
    final String PRODUCTNAME = "TestProduct";
    final Integer ORDERID = 12345;
    final Integer ORDERID2 = 54321;

    @PersistenceContext
    EntityManager entityManager;

    @Inject
    UserTransaction userTransaction;

    @Inject
    PurchaseRepository repository;

    @AfterEach void cleanup() {
        final String purchaseQuery = String.format("DELETE FROM %s", PurchaseEntity.class.getSimpleName());
        runInTransaction(() -> entityManager.createQuery(purchaseQuery).executeUpdate());
    }

    @Test
    void testAddOrder() {
        try {
            Item item = new Item();
            item.setPrice(10f);
            item.setProductId(PRODUCTID);
            item.setProductName(PRODUCTNAME);

            PurchaseEntity order = new PurchaseEntity();
            order.setId(ORDERID);
            order.setItems(List.of(item));
            order.setTotal(10f);
            order.setUserId(Integer.parseInt(USERID));

            userTransaction.begin();
            Integer orderId = repository.persistOrder(order);
            userTransaction.commit();

            assertEquals(order.getTotal(), repository.findOrderbyId(orderId).getTotal());
        } catch (Exception e) {
            throw new RuntimeException("caught exception: " + e.getMessage());
        }
    }

    @Test
    void testGetAllOrders() throws SecurityException, IllegalStateException, RollbackException {
        try {
            Item item = new Item();
            item.setPrice(10f);
            item.setProductId(PRODUCTID);
            item.setProductName(PRODUCTNAME);

            PurchaseEntity order = new PurchaseEntity();
            order.setId(ORDERID);
            order.setItems(List.of(item));
            order.setTotal(10f);
            order.setUserId(Integer.parseInt(USERID));

            PurchaseEntity order2 = new PurchaseEntity();
            order2.setId(ORDERID2);
            order2.setItems(List.of(item));
            order2.setTotal(20f);
            order2.setUserId(Integer.parseInt(USERID));

            userTransaction.begin();
            Integer orderId = repository.persistOrder(order);
            userTransaction.commit();

            userTransaction.begin();
            Integer orderId2 = repository.persistOrder(order2);
            userTransaction.commit();

            assertEquals(orderId, repository.findOrdersByUserId(Integer.parseInt(USERID)).get(0).getId());
            assertEquals(orderId2, repository.findOrdersByUserId(Integer.parseInt(USERID)).get(1).getId());

        }catch (Exception e) {
            try{
                userTransaction.rollback();
            } catch (Exception ex) {
                repository.getEntityManager().clear();
                throw new RuntimeException("caught exception: " + e.getMessage());
            }
            repository.getEntityManager().clear();
            throw new RuntimeException("caught exception: " + e.getMessage());
        }
    }
}
