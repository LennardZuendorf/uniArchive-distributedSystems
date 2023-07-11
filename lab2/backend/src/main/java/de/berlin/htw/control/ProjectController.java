package de.berlin.htw.control;

import de.berlin.htw.boundary.AlreadyExistsException;
import de.berlin.htw.entity.dao.ProjectRepository;
import de.berlin.htw.entity.dao.UserRepository;
import de.berlin.htw.entity.dto.ProjectEntity;
import de.berlin.htw.entity.dto.UserEntity;
import de.berlin.htw.lib.model.ProjectModel;
import de.berlin.htw.lib.model.UserModel;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.jboss.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lennard Zündorf [s0568383@htw-berlin.de]
 * mostly copied from UserController.java by Alexander Stanik
 */
@RequestScoped
public class ProjectController {
    
    @Inject
    Logger logger;

    @Inject
    ProjectRepository repository;

    @Inject
    UserRepository userRepository;

    @Inject
    UserTransaction transaction;

    @Transactional
    public String createProject(final ProjectModel project) {
        logger.infov("Creating a new project (name={0})", project.getName());

        final ProjectEntity entity = new ProjectEntity();
        entity.setName(project.getName());
        entity.setUsers(List.of());

        try {
            return repository.add(entity);
        } catch (EntityExistsException e) {
            logger.error("Unable to create project", e);
            throw new AlreadyExistsException("Unable to create project", e);
        } catch (Exception e) {
            logger.error("Unable to create project", e);
            throw new InternalServerErrorException("Unable to create project", e);
        }
    }

    public ProjectModel getProject(final String projectId) {
        logger.infov("Retrieving an existing project (id = {0})", projectId);

        final ProjectModel project = repository.get(projectId);

        if(project != null) {
            try{
                Hibernate.initialize(project.getUsers());
            } catch (HibernateException e){
                logger.error("unable to initalize users", e);
                throw new InternalServerErrorException("unable to initalize users", e);
            }
            return project;
        }else {
            logger.error("Project does not exist");
            throw new NotFoundException("Project does not exist");
        }
    }

    public List<? extends ProjectModel> getProjects() {
        logger.infov("Retrieving all existing projects");
        return repository.getAll();
    }

    @Transactional
    public ProjectModel updateProject(final ProjectModel project) {
        logger.infov("Updating an existing project ({0})", project);

        try{
            final ProjectEntity updateEntity = repository.get(project.getId());

            if(project.getName() != null) {
                updateEntity.setName(project.getName());
            }

            try {
                return repository.set(updateEntity);
            } catch (EntityExistsException e) {
                logger.error("Unable to update project", e);
                throw new AlreadyExistsException("Unable to update project", e);
            } catch (Exception e) {
                logger.error("Unable to update project", e);
                throw new InternalServerErrorException("Unable to update project", e);
            }

        } catch (NotFoundException e) {
            logger.error("Project does not exist", e);
            throw new NotFoundException("Project does not exist", e);
        }
    }

    @Transactional
    public ProjectEntity linkProject(String projectId, String userId) {
        logger.infov("adding a user ({0}) to an existing project ({1})", userId, projectId);
        try {
            final ProjectEntity updateEntity = repository.get(projectId);

            try {
                UserEntity addUser = userRepository.get(userId);

                List<UserEntity> updateUsers = updateEntity.getUsers();
                updateUsers.add(addUser);
                updateEntity.setUsers(updateUsers);

                logger.infov("updated project to: {0}", updateEntity);

                try{
                    return repository.set(updateEntity);
                } catch (EntityExistsException e) {
                    logger.error("Unable to update project", e);
                    throw new AlreadyExistsException("Unable to update project", e);
                } catch (Exception e) {
                    logger.error("Unable to update project", e);
                    throw new InternalServerErrorException("Unable to update project", e);
                }

            } catch (NotFoundException e) {
                logger.error("User does not exist", e);
                throw new NotFoundException("User does not exist", e);
            }


        } catch (NotFoundException e) {
            logger.error("Project does not exist", e);
            throw new NotFoundException("Project does not exist", e);
        }
    }

    //TODO: Update
    @Transactional
    public ProjectEntity unlinkProject(String projectId, String userId) {
        logger.infov("removing user ({0}) from an existing project ({1})", userId, projectId);

        try {
            final ProjectEntity updateEntity = repository.get(projectId);

            try {
                UserEntity removeUser = userRepository.get(userId);
                if (!updateEntity.getUsers().contains(removeUser)) throw new NotFoundException("User does not exist in project");

                List<UserEntity> updateUsers = updateEntity.getUsers();
                updateUsers.remove(removeUser);
                updateEntity.setUsers(updateUsers);

                logger.infov("updated project to: {0}", updateEntity);

                try{
                    return repository.set(updateEntity);
                } catch (EntityExistsException e) {
                    logger.error("Unable to update project", e);
                    throw new AlreadyExistsException("Unable to update project", e);
                } catch (Exception e) {
                    logger.error("Unable to update project", e);
                    throw new InternalServerErrorException("Unable to update project", e);
                }

            } catch (NotFoundException e) {
                logger.error("User does not exist", e);
                throw new NotFoundException("User does not exist", e);
            }

        } catch (NotFoundException e) {
            logger.error("Project does not exist", e);
            throw new NotFoundException("Project does not exist", e);
        }
    }

    @Transactional
    public boolean deleteProject(final String projectId) {
        logger.infov("Deleting an existing project (id = {0})", projectId);
        return repository.remove(projectId);
    }
}
