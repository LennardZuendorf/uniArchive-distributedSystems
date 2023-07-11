package de.berlin.htw.entity;
import de.berlin.htw.AbstractTest;
import de.berlin.htw.entity.dao.ProjectRepository;
import de.berlin.htw.entity.dao.UserRepository;
import de.berlin.htw.entity.dto.ProjectEntity;
import de.berlin.htw.entity.dto.UserEntity;
import io.quarkus.test.junit.QuarkusTest;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.TransactionalException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Lennard Zündorf [s0568383@htw-berlin.de]
 * partly copied from UserRepositoryTest.java by Alexander Stanik
 */
@QuarkusTest
class ProjectRepositoryTest  extends AbstractTest{

    static final String PROJECTNAME = "Test Project";

    static final String PROJECTNAME2 = "Test Project 2";
    static final String USERNAME = "Max Mustermann";
    static final String EMAIL = "max.mustermann@example.org";

    @Inject
    Logger logger;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    @AfterEach
    void cleanUp() throws Exception {
        if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
            userTransaction.rollback();
        }
    }

    @Test
    void testTransactionRequired() {
        assertThrows(
                TransactionalException.class,
                () -> projectRepository.add(new ProjectEntity()));
    }

    @Test
    void testGetNull() throws Exception{
        final String fakeProjectId = UUID.randomUUID().toString();

        userTransaction.begin();
        assertNull(projectRepository.get(fakeProjectId));
        userTransaction.commit();
        projectRepository.getEntityManager().clear();
    }

    @Test
    void testGetAll() throws Exception{
        final ProjectEntity entity = new ProjectEntity();
        entity.setName(PROJECTNAME);

        final ProjectEntity entity2 = new ProjectEntity();
        entity2.setName(PROJECTNAME2);

        userTransaction.begin();
        final String projectId = projectRepository.add(entity);
        assertNotNull(projectId);
        assertEquals(36, projectId.length());

        final String project2Id = projectRepository.add(entity2);
        assertNotNull(project2Id);
        assertEquals(36, project2Id.length());
        userTransaction.commit();
        projectRepository.getEntityManager().clear();

        assertEquals(2, projectRepository.getAll().size());
    }

    @Test
    void testAdd() throws Exception {
        final ProjectEntity entity = new ProjectEntity();
        entity.setName(PROJECTNAME);

        userTransaction.begin();
        final String projectId = projectRepository.add(entity);
        assertNotNull(projectId);
        assertEquals(36, projectId.length());
        userTransaction.commit();
        projectRepository.getEntityManager().clear();

        assertEquals(PROJECTNAME, projectRepository.get(projectId).getName());
    }

    @Test
    void testAddProjectAndUser() throws Exception {
        final UserEntity newUserEntity = new UserEntity();
        newUserEntity.setName(USERNAME);
        newUserEntity.setEmail(EMAIL);

        final ProjectEntity newProjectEntity = new ProjectEntity();
        newProjectEntity.setName(PROJECTNAME);

        userTransaction.begin();
        final String userId = userRepository.add(newUserEntity);
        assertNotNull(userId);
        assertEquals(36, userId.length());
        userTransaction.commit();
        userRepository.getEntityManager().clear();
        assertEquals(USERNAME, userRepository.get(userId).getName());

        userTransaction.begin();
        newProjectEntity.setUsers(List.of(userRepository.get(userId)));
        assertEquals(List.of(userRepository.get(userId)), newProjectEntity.getUsers());
        final String projectId = projectRepository.add(newProjectEntity);
        assertNotNull(projectId);
        assertEquals(36, projectId.length());
        userTransaction.commit();
        projectRepository.getEntityManager().clear();
        assertEquals(PROJECTNAME, projectRepository.get(projectId).getName());

        assertEquals(List.of(userRepository.get(userId)), projectRepository.get(projectId).getUsers());
    }

    @Test
    void testSet() throws Exception{
        final ProjectEntity entity = new ProjectEntity();
        entity.setName(PROJECTNAME);

        userTransaction.begin();
        final String projectId = projectRepository.add(entity);
        assertNotNull(projectId);
        assertEquals(36, projectId.length());
        userTransaction.commit();

        userTransaction.begin();
        final ProjectEntity updateEntity = projectRepository.get(projectId);
        updateEntity.setName(PROJECTNAME2);
        projectRepository.set(updateEntity);
        userTransaction.commit();
        projectRepository.getEntityManager().clear();

        assertEquals(updateEntity.getName(), projectRepository.get(projectId).getName());
    }

    @Test
    void testSetUser() throws Exception{

        //creating and adding a userEntity
        final UserEntity newUserEntity = new UserEntity();
        newUserEntity.setName(USERNAME);
        newUserEntity.setEmail(EMAIL);

        userTransaction.begin();
        final String userId = userRepository.add(newUserEntity);
        assertNotNull(userId);
        assertEquals(36, userId.length());
        userTransaction.commit();
        userRepository.getEntityManager().clear();
        assertEquals(USERNAME, userRepository.get(userId).getName());

        //creating and adding a projectEntity
        final ProjectEntity newProjectEntity = new ProjectEntity();
        newProjectEntity.setName(PROJECTNAME);

        userTransaction.begin();
        final String projectId = projectRepository.add(newProjectEntity);
        assertNotNull(projectId);
        assertEquals(36, projectId.length());
        userTransaction.commit();

        assertEquals(PROJECTNAME, projectRepository.get(projectId).getName());
        assertEquals(0,projectRepository.get(projectId).getUsers().size());

        final ProjectEntity updateProjectEntity = projectRepository.get(projectId);
        final List<UserEntity> newUsers = updateProjectEntity.getUsers();
        newUsers.add(userRepository.get(userId));
        updateProjectEntity.setUsers(newUsers);
        assertEquals(List.of(userRepository.get(userId)), updateProjectEntity.getUsers());

        userTransaction.begin();
        projectRepository.set(updateProjectEntity);
        userTransaction.commit();
        projectRepository.getEntityManager().clear();
        assertEquals(PROJECTNAME, projectRepository.get(projectId).getName());

        assertEquals(List.of(userRepository.get(userId)), projectRepository.get(projectId).getUsers());
    }

    @Test
    void testAddAndRemove() throws Exception {
        final ProjectEntity entity = new ProjectEntity();
        entity.setName(PROJECTNAME);

        userTransaction.begin();
        final String projectId = projectRepository.add(entity);
        assertNotNull(projectId);
        assertEquals(36, projectId.length());
        projectRepository.remove(projectId);
        userTransaction.commit();
        projectRepository.getEntityManager().clear();

        assertNull(projectRepository.get(projectId));
    }
}