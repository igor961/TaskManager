package org.example.dao;

import org.example.entities.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProjectDaoTest {
    @Autowired
    private ProjectDao dao;

    @Test
    public void save() {
        String name = "test3";
        long expectedQ = dao.getAll().stream().filter(e -> e.name.equals(name)).count() + 1;
        Project p = new Project(0, name);
        dao.save(p);
        List<Project> aux = dao.getAll();
        long realQ = aux.stream().filter(e -> e.name.equals(name)).count();
        System.out.println(aux.get(aux.size()-1));
        assertEquals(expectedQ, realQ);
    }

    @Test
    public void getById() {
        Project res = dao.get(40).get();
        System.out.println(res);
        assertThat(res).isNotNull();
    }

    @Test
    public void getAll() {
        List<Project> res = dao.getAll();
        assertThat(res).isNotNull();
    }

    @Test
    public void update() {
        int id = 42;
        dao.update(new Project(id, "newName"));
        assertEquals("newName", dao.get(id).get().name);
    }

    @Test
    public void delete() {
        int id = 41;
        dao.delete(id);
        assertThat(dao.get(id)).isNotPresent();
    }
}
