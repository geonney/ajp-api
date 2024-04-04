package com.aljjabaegi.api.domain.project;

import com.aljjabaegi.api.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
