package org.example.dao;

import org.example.dto.TaskDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class TaskDao implements BasicDao<TaskDto> {
    private final JdbcTemplate jdbcTemplate;

    public TaskDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TaskDto get(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT t.id, t.name, t.status, t.priority, t.project_id, p.name as project_name, get_aux_id_for_tasks(t.priority, t.id, (SELECT max(id) FROM tasks)) as aux_id FROM tasks t INNER JOIN projects p ON p.id = t.project_id WHERE t.id = ?;",
                new Object[]{id},
                (rs, i) -> new TaskDto(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBoolean("status"),
                        rs.getLong("project_id"),
                        rs.getString("project_name"),
                        rs.getInt("priority"),
                        rs.getLong("aux_id"),
                        rs.getTimestamp("term")));
    }


    @Override
    public TaskDto save(TaskDto task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps =
                    con.prepareStatement(
                            "INSERT INTO tasks (name, status, project_id) VALUES (?, ?, ?);",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.name);
            ps.setBoolean(2, task.status);
            ps.setLong(3, task.projectId);
            return ps;
        }, keyHolder);
        return this.get(Long.parseLong(keyHolder.getKeys().get("id").toString()));
    }

    @Override
    public void update(TaskDto task) {
        jdbcTemplate.update(
                "UPDATE tasks SET name = COALESCE(?, name), " +
                        "status = COALESCE(?, status), " +
                        "priority = COALESCE(?, priority), " +
                        "term = COALESCE(?, term) WHERE id = ?;",
                task.name, task.status, task.priority, task.term, task.id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?;", id);
    }
}
