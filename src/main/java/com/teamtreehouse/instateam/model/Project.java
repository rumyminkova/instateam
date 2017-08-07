package com.teamtreehouse.instateam.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * Created by Rumy on 7/16/2017.
 */


@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;

    @NotNull
    @Size(min=3, max=30,message = "The project name must be {min} to {max} characters in length.")
    private String name;

    @NotNull
    @Size(min=3, max=50,message = "The project description must be {min} to {max} characters in length.")
    private String description;

    private String status;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "project_roles",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> rolesNeeded=new HashSet<>();


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "project_collaborators",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "collaborator_id")
    )
    private Set<Collaborator> collaborators=new HashSet<>();

    public Project() {
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Role> getRolesNeeded() {
        return rolesNeeded;
    }

    public void setRolesNeeded(Set<Role> rolesNeeded) {
        this.rolesNeeded = rolesNeeded;
    }


    public Set<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Set<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public void addCollaborator(Collaborator collaborator){
        collaborators.add(collaborator);
    }

    public boolean removeCollaborator(Collaborator collaborator){
        return collaborators.remove(collaborator);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        if (project_id != null ? !project_id.equals(project.project_id) : project.project_id != null) return false;
        if (name != null ? !name.equals(project.name) : project.name != null) return false;
        if (description != null ? !description.equals(project.description) : project.description != null) return false;
        if (status != null ? !status.equals(project.status) : project.status != null) return false;
        if (rolesNeeded != null ? !rolesNeeded.equals(project.rolesNeeded) : project.rolesNeeded != null) return false;
        return collaborators != null ? collaborators.equals(project.collaborators) : project.collaborators == null;
    }

    @Override
    public int hashCode() {
        int result = project_id != null ? project_id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (rolesNeeded != null ? rolesNeeded.hashCode() : 0);
        result = 31 * result + (collaborators != null ? collaborators.hashCode() : 0);
        return result;
    }

}

