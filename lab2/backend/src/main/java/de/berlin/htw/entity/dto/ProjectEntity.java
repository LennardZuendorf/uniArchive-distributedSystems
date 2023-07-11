package de.berlin.htw.entity.dto;

import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import de.berlin.htw.lib.model.UserModel;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import de.berlin.htw.lib.model.ProjectModel;

/**
 * @author Lennard Zündorf [s0568383@htw-berlin.de]
 * mostly copied from UserEntity.java by Alexander Stanik
 */
@NamedQuery(name = "ProjectEntity.deleteById", query = "delete from ProjectEntity project where project.id = :id")
@Entity
@Table(name = "PROJECT")
public class ProjectEntity extends AbstractEntity implements ProjectModel {

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PROJECT_USER_JUNCTION",
            joinColumns = @JoinColumn(name = "PROJECT_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name="USERS")
    private List<UserEntity> users;

    public String getId() {
        return id.toString();
    }
    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<UserEntity> getUsers() {
        return this.users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
