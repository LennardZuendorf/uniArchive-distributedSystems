package de.berlin.htw.lib.dto;

import de.berlin.htw.lib.model.ProjectModel;
import de.berlin.htw.lib.model.UserModel;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.validation.constraints.Null;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Lennard Zündorf [s0568383@htw-berlin.de]
 * mostly copied from UserJson.java by Alexander Stanik
 */

public class ProjectJson implements ProjectModel {

    @Null
    private String id;

    @JsonbProperty("project_name")
    @Size(min = 3, max = 99, message = "The name must be between 3 and 99 characters")
    private String name;

    @JsonbProperty("users")
    private List<? extends UserModel> users;

    @JsonbProperty("registered_date")
    @JsonbDateFormat("dd-MM-yyyy")
    @PastOrPresent
    private LocalDate registeredDate;

    @JsonbTransient
    private Integer age;

    public ProjectJson() {
        // do nothing
    }

    public ProjectJson(ProjectModel project) {
        this.id = project.getId();
        this.name = project.getName();
        this.users = project.getUsers();
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public List<? extends UserModel> getUsers() {
        return this.users;
    }

    public void setUsers(final List<? extends UserModel> users) {
        this.users = users;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(final LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }
}
