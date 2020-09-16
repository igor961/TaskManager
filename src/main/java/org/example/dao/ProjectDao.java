package org.example.dao;

import org.example.dto.ProjectDto;
import org.example.dto.TasksBatchDto;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

@Repository
public class ProjectDao implements BasicDao<ProjectDto> {
    private final JdbcTemplate jdbcTemplate;

    public ProjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProjectDto> getProjectsWithTasks() {
        String query = "WITH max_id AS (SELECT MAX(id) FROM tasks), " +
                "next_ids AS (SELECT id, lead(get_aux_id_for_tasks(priority, id, (select * from max_id))) OVER (PARTITION BY project_id ORDER BY priority) as next_id FROM tasks)" +
                "SELECT p.id, p.name, json_object_agg(" +
                "get_aux_id_for_tasks(t.priority, t.id, (select * from max_id)), " +
                "to_jsonb(t.*) #- '{project_id}' || jsonb_build_object(" +
                "'projectId', t.project_id, " +
                "'auxId', get_aux_id_for_tasks(t.priority, t.id, (select * from max_id)), " +
                "'nextId', (SELECT res.next_id FROM next_ids as res WHERE res.id = t.id)" +
                ") " +
                "order by t.priority) as tasks " +
                "FROM tasks t INNER JOIN projects p ON p.id = t.project_id " +
                "GROUP BY p.id, p.name;";
        return jdbcTemplate.query(query,
                (rs, i) -> new ProjectDto(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("tasks")));
    }

    public void saveTasks(TasksBatchDto project) {
        this.jdbcTemplate.batchUpdate(
                "UPDATE tasks SET status = COALESCE(?, status), priority = ? WHERE id = ?;",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        var task = project.tasks.get(i);
                        ps.setBoolean(1, task.status);
                        ps.setInt(2, task.priority);
                        ps.setLong(3, task.id);
                    }

                    @Override
                    public int getBatchSize() {
                        return project.getSize();
                    }
                });
    }

    @Override
    public ProjectDto get(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id as project_id, name from projects WHERE id = ?;",
                new Object[]{id},
                new int[]{Types.INTEGER},
                ((rs, rowNum) -> new ProjectDto(rs.getLong("id"), rs.getString("name"))));
    }

    @Override
    public ProjectDto save(ProjectDto project) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO projects (name) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.name);
            return ps;
        }, keyHolder);
        return new ProjectDto(Long.parseLong(keyHolder.getKeys().get("id").toString()), project.name);
    }

    @Override
    public void update(ProjectDto project) {
        jdbcTemplate.update("UPDATE projects SET name = ? WHERE id = ?;", project.name, project.id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM projects WHERE id = ?;", id);
    }
}
