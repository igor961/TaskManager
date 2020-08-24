package org.example.entities;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {
    public final long id;
    public final String name;
    public final Boolean status;
    public final long projectId;

    public Task(long id, String name, Boolean status, long projectId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", projecId=" + projectId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                projectId == task.projectId &&
                Objects.equals(name, task.name) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, projectId);
    }
}
