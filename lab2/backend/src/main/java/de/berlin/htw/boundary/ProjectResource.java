package de.berlin.htw.boundary;

import de.berlin.htw.control.ProjectController;
import de.berlin.htw.lib.ProjectEndpoint;
import de.berlin.htw.lib.dto.ProjectJson;
import de.berlin.htw.lib.model.ProjectModel;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lennard Zündorf [s0568383@htw-berlin.de]
 * mostly copied from UserResource.java by Alexander Stanik
 */
public class ProjectResource implements ProjectEndpoint {

    @Context
    UriInfo uri;

    @Inject
    ProjectController controller;

    @Override
    public Response createProject(ProjectJson project) {
        final String projectId = controller.createProject(project);
        final URI location = uri.getAbsolutePathBuilder().path(projectId).build();
        return Response.created(location).build();
    }

    @Override
    public List<ProjectJson> getProjects() {
        final List<ProjectJson> projects = new ArrayList<>();
        controller.getProjects().forEach(project -> projects.add(new ProjectJson(project)));
        return projects;
    }

    @Override
    public ProjectJson linkUser(String projectId, String userId) {
        final ProjectModel updatedProject = controller.linkProject(projectId, userId);
        return new ProjectJson(updatedProject);
    }

    @Override
    public ProjectJson unlinkUser(String projectId, String userId) {
        final ProjectModel updatedProject = controller.unlinkProject(projectId, userId);
        return new ProjectJson(updatedProject);
    }

    @Override
    public ProjectJson getProject(String projectId) {
        try {
            final ProjectModel project = controller.getProject(projectId);
            return new ProjectJson(project);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid project id", e);
        }
    }

    @Override
    public ProjectJson updateProject(String projectId, ProjectJson project) {
        if (project.getId() != null) {
            throw new BadRequestException("Project ID should not be set in payload");
        } else {
            project.setId(projectId);
        }
        final ProjectModel updatedProject = controller.updateProject(project);
        return new ProjectJson(updatedProject);
    }

    @Override
    public void deleteProject(String projectId) {
        if (!controller.deleteProject(projectId)) {
            throw new NotFoundException();
        }
    }
}
