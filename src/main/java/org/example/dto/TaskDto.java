package org.example.dto;

import java.io.Serializable;
import java.util.Objects;

public class TaskDto implements Serializable {
    public final long id;
    public final String name;
    public final Boolean status;
    public final long projecId;

    public TaskDto(long id, String name, Boolean status, long projectId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.projecId = projectId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", projecId=" + projecId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto task = (TaskDto) o;
        return id == task.id &&
                projecId == task.projecId &&
                Objects.equals(name, task.name) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, projecId);
    }
}
