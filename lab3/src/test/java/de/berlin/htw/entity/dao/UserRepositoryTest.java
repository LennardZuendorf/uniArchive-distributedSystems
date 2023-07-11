package de.berlin.htw.entity.dao;

import de.berlin.htw.AbstractTest;
import de.berlin.htw.entity.dto.UserEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class UserRepositoryTest extends AbstractTest {

    final String USERNAME = "TestUser";
    @Inject
    UserRepository repository;
    @Inject
    EntityManager entityManager;

    @AfterEach void cleanup() {
        runInTransaction(() -> entityManager.createQuery("DELETE FROM UserEntity u WHERE u.name = :userName")
                .setParameter("userName", USERNAME)
                .executeUpdate());
    }

    @Test
    void testAddUser(){
        try {
            userTransaction.begin();
            UserEntity user = new UserEntity();
            user.setName(USERNAME);
            user.setBalance(10f);
            Integer userId = repository.persistUser(user);
            userTransaction.commit();
            repository.getEntityManager().clear();

            assertEquals(userId, repository.findUserById(userId).getId());
            assertEquals(user.getBalance(), repository.findUserById(userId).getBalance());

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

    @Test
    void updateUser() {
        try {
            userTransaction.begin();
            UserEntity user = new UserEntity();
            user.setName(USERNAME);
            user.setBalance(10f);
            Integer userId = repository.persistUser(user);
            userTransaction.commit();

            assertEquals(userId, repository.findUserById(userId).getId());
            assertEquals(user.getBalance(), repository.findUserById(userId).getBalance());

            userTransaction.begin();
            UserEntity updateUser = repository.findUserById(userId);
            updateUser.setBalance(50f);
            repository.updateUser(updateUser);
            userTransaction.commit();
            repository.getEntityManager().clear();

            assertEquals(userId, repository.findUserById(userId).getId());
            assertEquals(updateUser.getBalance(), repository.findUserById(userId).getBalance());

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