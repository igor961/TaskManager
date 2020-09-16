package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class BatchTaskDto extends TaskDto {
    @JsonCreator
    public BatchTaskDto(long id, String name, Boolean status, long projectId, int priority) {
        super(id, name, status, projectId, null, priority, 0, null);
    }
}
