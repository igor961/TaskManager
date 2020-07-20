package org.example.entities;

import java.io.Serializable;

public class Project implements Serializable {
    public final int id;
    public final String name;

    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
