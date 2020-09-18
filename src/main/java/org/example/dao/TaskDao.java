package org.example.dao;

import org.example.dto.TaskDto;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;

@Repository
public class TaskDao implements BasicDao<TaskDto> {
    private final JdbcTemplate jdbcTemplate;

    public TaskDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TaskDto get(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT t.*, p.name as project_name, get_aux_id_for_tasks(t.priority, t.id, (SELECT max(id) FROM tasks)) as aux_id FROM tasks t INNER JOIN projects p ON p.id = t.project_id WHERE t.id = ?;",
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

    public void saveTasks(List<TaskDto> batch) {
        this.jdbcTemplate.batchUpdate(
                "UPDATE tasks SET status = COALESCE(?, status), priority = ? WHERE id = ?;",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        var task = batch.get(i);
                        ps.setBoolean(1, task.status);
                        ps.setInt(2, task.priority);
                        ps.setLong(3, task.id);
                    }

                    @Override
                    public int getBatchSize() {
                        return batch.size();
                    }
                });
    }

    @Override
    public void update(TaskDto task) {
        jdbcTemplate.update(
                "UPDATE tasks SET name = COALESCE(?, name), " +
                        "status = COALESCE(?, status), " +
                        "priority = COALESCE(?, priority), " +
                        "term = ? WHERE id = ?;",
                task.name, task.status, task.priority, task.term, task.id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?;", id);
    }
}
