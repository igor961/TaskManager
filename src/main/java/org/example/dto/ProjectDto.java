package org.example.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProjectDto implements Serializable {
    public final long id;
    public final String name;

    public ProjectDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProjectDto(String name) {
        this.id = 0;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectDto project = (ProjectDto) o;
        return id == project.id &&
                Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
