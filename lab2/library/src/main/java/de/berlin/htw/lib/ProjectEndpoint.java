package de.berlin.htw.lib;

import de.berlin.htw.lib.dto.ProjectJson;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Lennard Zündorf [s0568383@htw-berlin.de]
 *
 */
@Path(ProjectEndpoint.CONTEXT + "/" + ProjectEndpoint.VERSION + "/" + ProjectEndpoint.SERVICE)
public interface ProjectEndpoint {

    final static String CONTEXT = "api";
    final static String VERSION = "v2";
    final static String SERVICE = "projects";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve information of all projects.")
    List<ProjectJson> getProjects();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new projects.")
    @APIResponse(responseCode = "201", description = "project created successfully",
        headers = @Header(name = "Location", description = "URL to retrieve all orders"))
    @APIResponse(responseCode = "400", description = "Invalid request message")
    @APIResponse(responseCode = "409", description = "Another project with the same name already exist")
    Response createProject(
        @Parameter(description = "Project information", required = true) @Valid ProjectJson project);

    @PATCH
    @Path("/{projectId}/users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update information of a project.")
    @APIResponse(responseCode = "404", description = "The project or user do not exist")
    ProjectJson linkUser(
            @Parameter (description = "ID of the project and the new User", required = true)
            @PathParam("projectId") String projectId, @PathParam("userId") String userId);

    @DELETE
    @Path("/{projectId}/users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "removing a user from a project.")
    @APIResponse(responseCode = "404", description = "The project or user do not exist")
    ProjectJson unlinkUser(
            @Parameter (description = "ID of the project and the new User", required = true) @PathParam("projectId") String projectId, @PathParam("userId") String userId);

    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve information of a project.")
    @APIResponse(responseCode = "404", description = "The project does not exist")
    ProjectJson getProject(
        @Parameter(description = "ID of the project", required = true) @PathParam("projectId") String projectId);

    @PATCH
    @Path("/{projectId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update information of a project.")
    @APIResponse(responseCode = "404", description = "The project does not exist")
    ProjectJson updateProject(
        @Parameter(description = "ID of the project", required = true) @PathParam("projectId") String projectId,
        @Parameter(description = "Project information", required = true) @Valid ProjectJson project);

    @DELETE
    @Path("/{projectId}")
    @Operation(summary = "Delete an existing project.")
    @APIResponse(responseCode = "204", description = "The project was deleted successfully")
    @APIResponse(responseCode = "404", description = "The project does not exist")
    void deleteProject(
        @Parameter(description = "ID of the project", required = true) @PathParam("projectId") String projectId);
}
