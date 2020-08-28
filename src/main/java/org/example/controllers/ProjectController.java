package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.ProjectDao;
import org.example.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class ProjectController {
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectDao dao;

    @Autowired
    public ProjectController(ProjectDao dao) {
        this.dao = dao;
    }

    @MessageMapping("/all")
    @SendToUser("/queue/projects-with-tasks")
    public List<ProjectDto> all(Principal p) throws Exception {
        logger.info("Get all projects with tasks");
        return dao.getProjectsWithTasks();
    }

    @MessageMapping("/test")
    @SendToUser("/queue/reply")
    public String hello(@Payload String m, Principal p) throws Exception {
        logger.info(m);
        return "hello";
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
