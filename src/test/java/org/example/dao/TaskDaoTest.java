package org.example.dao;

import org.example.dto.TaskDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class TaskDaoTest {

    @Autowired
    TaskDao dao;


    @Test
    public void save() {
        String name = "test10";
        long expectedQ = dao.getAll().stream().filter(e -> e.name.equals(name)).count() + 1;
        TaskDto p = new TaskDto(name, false, 43);
        dao.save(p);
        List<TaskDto> aux = dao.getAll();
        long realQ = aux.stream().filter(e -> e.name.equals(name)).count();
        System.out.println(aux.get(aux.size()-1));
        assertEquals(expectedQ, realQ);
    }

}