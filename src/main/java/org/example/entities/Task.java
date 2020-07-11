package org.example.entities;

public class Task {
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
}
