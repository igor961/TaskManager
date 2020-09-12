package org.example.controllers;

import org.example.dao.ProjectDao;
import org.example.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectDao dao;

    @Autowired
    public ProjectController(ProjectDao dao) {
        this.dao = dao;
    }

    @MessageMapping("/project/{id}")
    @SendToUser("/queue/project")
    public Optional<ProjectDto> get(@DestinationVariable long id) {
        logger.info("Get project by id");
        return dao.get(id);
    }

    @MessageMapping("/project/create")
    @SendToUser("/queue/project")
    public ProjectDto save(@Payload ProjectDto project) throws Exception {
        logger.info("Save project");
        logger.info("Project: " + project);
        return dao.save(project);
    }

    @MessageMapping("/project/update")
    @SendToUser("/queue/project")
    public void update(@Payload ProjectDto project) {
        logger.info("Update project");
        logger.info("Project: " + project);
        dao.update(project);
    }

    @MessageMapping("/project/delete/{id}")
    @SendToUser("/queue/project")
    public void delete(@DestinationVariable long id) {
        logger.info("Delete project");
        dao.delete(id);
    }

    @MessageMapping("/all")
    @SendToUser("/queue/projects-with-tasks")
    public List<ProjectDto> all() throws Exception {
        logger.info("Get all projects with tasks");
        return dao.getProjectsWithTasks();
    }

    @MessageMapping("/test")
    @SendToUser("/queue/reply")
    public String hello(@Payload String m) throws Exception {
        logger.info(m);
        return "hello";
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
