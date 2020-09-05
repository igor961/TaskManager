package org.example;

import org.example.entities.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Types;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenTomcatConnectionPoolInstance_whenCheckedPoolClassName_thenCorrect() {
        assertThat(dataSource.getClass().getName()).isEqualTo("org.apache.tomcat.jdbc.pool.DataSource");
    }

    @Test
    public void jdbcTemplateIsNotNull() {
        assertThat(jdbcTemplate).isNotNull();
    }

    @Test
    public void jdbcTemplateInsertQueryDataExists() {
        Object str = System.currentTimeMillis();
        Object[] param = new Object[]{str};
        int[] type = {Types.VARCHAR};
        jdbcTemplate.update("INSERT INTO projects (name) VALUES (?);", param, type);
        Project res = jdbcTemplate.queryForObject(
                "SELECT id, name FROM projects WHERE name LIKE ?;",
                param,
                type,
                (rs, i) -> new Project(
                        rs.getInt("id"),
                        rs.getString("name"))
        );
        assertThat(res).isNotNull();
    }
}
