package org.example.dao;

import org.example.dto.ProjectDto;
import org.example.dto.mappers.ModelMapper;
import org.example.entities.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProjectDao implements BasicDao<ProjectDto> {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Project> rowMapper = (rs, i) ->
            new Project(rs.getInt("id"), rs.getString("name"));

    private final ModelMapper<Project, ProjectDto> modelMapper = new ModelMapper<>() {
        @Override
        public ProjectDto getDto(Project project) {
            return new ProjectDto(project.id, project.name);
        }

        @Override
        public Project getEntity(ProjectDto projectDto) {
            return new Project(projectDto.id, projectDto.name);
        }
    };

    public ProjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ProjectDto> get(int id) {
        Project res = jdbcTemplate.queryForObject(
                "SELECT * from projects WHERE id = ?;",
                new Object[]{id},
                new int[]{Types.INTEGER},
                rowMapper);
        return Optional.ofNullable(modelMapper.getDto(res));
    }

    @Override
    public List<ProjectDto> getAll() {
        return jdbcTemplate.query("SELECT * from projects;", rowMapper)
                .stream()
                .map(modelMapper::getDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto save(ProjectDto project) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO projects (name) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.name);
            return ps;
        }, keyHolder);
        return new ProjectDto((int) keyHolder.getKeys().get("id"), project.name);
    }

    @Override
    public void update(ProjectDto project) {
        jdbcTemplate.update("UPDATE projects SET name = ? WHERE id = ?;", project.name, project.id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM projects WHERE id = ?;", id);
    }
}
