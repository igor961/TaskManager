package org.example.controllers;

import org.example.dao.TaskDao;
import org.example.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class TaskController {
    private final TaskDao dao;

    @Autowired
    public TaskController(TaskDao dao) {
        this.dao = dao;
    }

    @MessageMapping("/task/create")
    @SendToUser("/queue/task")
    public TaskDto save(@Payload TaskDto task) {
        return dao.save(task);
    }

    @MessageMapping("/task/batch")
    public void saveTasks(@Payload List<TaskDto> project) throws Exception {
        dao.saveTasks(project);
    }

    @MessageMapping("/task/update")
    @SendToUser("/queue/task")
    public void update(@Payload TaskDto task) {
        dao.update(task);
    }

    @MessageMapping("/task/delete/{id}")
    @SendToUser("/queue/task")
    public void delete(@DestinationVariable long id) {
        dao.delete(id);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
