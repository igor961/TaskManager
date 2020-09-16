package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class TasksBatchDto {
    public final long id;
    public final List<BatchTaskDto> tasks;

    @JsonCreator
    public TasksBatchDto(long id, List<BatchTaskDto> tasks) {
        this.id = id;
        this.tasks = tasks;
    }

    public int getSize() {
        return this.tasks.size();
    }
}
