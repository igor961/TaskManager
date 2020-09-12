package org.example.entities;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {
    public final long id;
    public final String name;
    public final Boolean status;
    public final Project project;
    public final int priority;

    public Task(long id, String name, Boolean status, Project project, int priority) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.project = project;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", projecId=" + project.id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
