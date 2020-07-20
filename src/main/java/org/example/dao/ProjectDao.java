package org.example.dao;

import org.example.entities.Project;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

@Repository
public class ProjectDao implements BasicDao<Project> {
    private final JdbcTemplate jdbcTemplate;
    private final HashOperations<String, Integer, Project> hashOperations;

    private final RowMapper<Project> rowMapper = (rs, i) ->
            new Project(rs.getInt("id"), rs.getString("name"));

    public ProjectDao(JdbcTemplate jdbcTemplate, RedisTemplate redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Optional<Project> get(int id) {
        return Optional.ofNullable((Project) hashOperations.get("project", id)).or(() -> {
            try {
                Project res = jdbcTemplate.queryForObject(
                        "SELECT * from projects WHERE id = ?;",
                        new Object[]{id},
                        new int[]{Types.INTEGER},
                        rowMapper);
                if (res != null) hashOperations.put("project", id, res);
                return Optional.ofNullable(res);
            } catch (EmptyResultDataAccessException e) {
                System.out.println(e.getMessage());
            }
            return Optional.empty();
        });
    }

    @Override
    public List<Project> getAll() {
        List<Project> res = hashOperations.values("projects");
        if (res.isEmpty()) {
            res = jdbcTemplate.query("SELECT * from projects;", rowMapper);
            res.forEach(p -> hashOperations.put("project", p.id, p));
        }
        return res;
    }

    @Override
    public Project save(Project project) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO projects (name) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.name);
            return ps;
        }, keyHolder);
        Project res = new Project((int) keyHolder.getKeys().get("id"), project.name);
        hashOperations.put("project", res.id, res);
        return res;
    }

    @Override
    public Project update(Project project) {
        hashOperations.delete("project", project.id);
        hashOperations.put("project", project.id, project);
        jdbcTemplate.update("UPDATE projects SET name = ? WHERE id = ?;", project.name, project.id);
        return project;
    }

    @Override
    public void delete(int id) {
        hashOperations.delete("project", id);
        jdbcTemplate.update("DELETE FROM projects WHERE id = ?;", id);
    }
}
