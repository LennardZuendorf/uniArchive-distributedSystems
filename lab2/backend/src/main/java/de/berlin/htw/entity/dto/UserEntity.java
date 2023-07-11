package de.berlin.htw.entity.dto;

import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.Email;

import de.berlin.htw.lib.model.ProjectModel;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import de.berlin.htw.lib.model.UserModel;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@NamedQuery(name = "UserEntity.deleteById", query = "delete from UserEntity user where user.id = :id")
@Entity
@Table(name = "USER")
public class UserEntity extends AbstractEntity implements UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = "ID", nullable = false, length = 36)
    private UUID id;
    
    @Column(name = "NAME")
    private String name;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    @Column(name="PROJECTS")
    private List<ProjectEntity> projects;
    
    @Email
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Override
    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectEntity> projects) {
        this.projects = projects;
    }
}
