package org.example.dto.mappers;

import org.example.dto.TaskDto;
import org.example.entities.Task;

public class TaskMapper implements ModelMapper<Task, TaskDto> {

    @Override
    public TaskDto getDto(Task task) {
        return new TaskDto(task.id, task.name, task.status, task.project.id, task.project.name, task.priority);
    }

}
