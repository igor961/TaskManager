package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.util.Objects;

public class TaskDto implements Serializable {
    public final long id;
    public final String name;
    public final Boolean status;
    public final long projectId;
    public final String projectName;
    public final int priority;

    public TaskDto(long id, String name, Boolean status, long projectId, String projectName, int priority) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.projectId = projectId;
        this.projectName = projectName;
        this.priority = priority;
    }

    @JsonCreator
    public TaskDto(String name, int priority, long projectId) {
        this.id = 0;
        this.name = name;
        this.status = false;
        this.projectId = projectId;
        this.projectName = null;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return id == taskDto.id &&
                projectId == taskDto.projectId &&
                Objects.equals(name, taskDto.name) &&
                Objects.equals(status, taskDto.status) &&
                Objects.equals(projectName, taskDto.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, projectId, projectName);
    }
}
