package org.example.entities;

import java.io.Serializable;

public class Task implements Serializable {
    public final int id;
    public final String name;
    public final Boolean status;
    public final int projecId;

    public Task(int id, String name, Boolean status, int projecId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.projecId = projecId;
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
}
