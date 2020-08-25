package org.example.dao;

import org.example.dto.TaskDto;
import org.example.dto.mappers.TaskMapper;
import org.example.entities.Project;
import org.example.entities.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TaskDao implements BasicDao<TaskDto> {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Task> rowMapper = (rs, i) -> new Task(rs.getLong("id"),
            rs.getString("name"),
            rs.getBoolean("status"),
            new Project(rs.getLong("project_id"), rs.getString("project_name")));

    private final TaskMapper modelMapper = new TaskMapper();

    public TaskDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*public List<TaskDto> getByProjectId(long id) {
        return
    }*/

    @Override
    public Optional<TaskDto> get(long id) {
        Task res = jdbcTemplate.queryForObject(
                "SELECT t.id, t.name, t.status, t.project_id, p.name as project_name from tasks t, projects p WHERE t.id = ?;",
                new Object[]{id},
                new int[]{Types.INTEGER},
                rowMapper);
        return Optional.ofNullable(modelMapper.getDto(res));
    }

    @Override
    public List<TaskDto> getAll() {
        return jdbcTemplate.query("SELECT t.*, p.name as project_name FROM tasks t LEFT JOIN projects p ON p.id = t.project_id;", rowMapper)
                .stream()
                .map(modelMapper::getDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto save(TaskDto task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO tasks (name, status, project_id) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.name);
            ps.setBoolean(2, task.status);
            ps.setLong(3, task.projectId);
            return ps;
        }, keyHolder);
        return new TaskDto((int) keyHolder.getKeys().get("id"), task.name, task.status, task.projectId, task.projectName);
    }

    @Override
    public void update(TaskDto task) {
        jdbcTemplate.update("UPDATE tasks SET name = ? WHERE id = ?;", task.name, task.id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?;", id);
    }
}
