package com.aljjabaegi.api.domain.project;

import com.aljjabaegi.api.domain.project.record.ProjectSearchResponse;
import com.aljjabaegi.api.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Project Repository
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    @Query(value = "select CONCAT('PJ', RIGHT('000' || (CAST(SUBSTRING(MAX(project_id) FROM '\\d+') AS INTEGER) + 1), 3)) FROM project"
            , nativeQuery = true)
    String findByMaxProjectId();

    @Query(nativeQuery = true)
    List<ProjectSearchResponse> findByProjectName(@Param("projectName") String projectName);
}
