package org.example.controllers;

import org.example.dao.TaskDao;
import org.example.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class TaskController {
    private final TaskDao dao;

    @Autowired
    public TaskController(TaskDao dao) {
        this.dao = dao;
    }

    @MessageMapping("/task/create")
    @SendToUser("/queue/task")
    public TaskDto save(TaskDto task) {
        return dao.save(task);
    }

    @MessageMapping("/task/update")
    @SendToUser("/queue/task")
    public void update(TaskDto task) {
        dao.update(task);
    }

    @MessageMapping("/task/delete")
    @SendToUser("/queue/task")
    public void delete(long id) {
        dao.delete(id);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
