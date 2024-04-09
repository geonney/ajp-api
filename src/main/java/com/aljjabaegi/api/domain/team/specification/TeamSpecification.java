package com.aljjabaegi.api.domain.team.specification;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.entity.Team;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Create Team Specification
 *
 * @author GEONLEE
 * @since 2024-04-09
 */
public class TeamSpecification {

    /**
     * Generate team name like specification
     *
     * @param teamName team name
     * @return like team name specification
     */
    public static Specification<Team> likeTeamName(String teamName) {
        return ((root, query, criteriaBuilder) -> {
            if (teamName == null) {
                return criteriaBuilder.conjunction();
            }
            Path<String> teamNamePath = root.get("teamName");
            return criteriaBuilder.like(teamNamePath, "%" + teamName + "%");
        });
    }

    /**
     * Generate create date between specification
     *
     * @param startDateString search start date
     * @param endDateString   search end date
     * @return between create date specification
     */
    public static Specification<Team> betweenCreateDate(String startDateString, String endDateString) {
        return ((root, query, criteriaBuilder) -> {
            Path<LocalDateTime> createDatePath = root.get("createDate");
            LocalDateTime startDate = Converter.dateTimeStringToLocalDateTime(startDateString);
            LocalDateTime endDate = Converter.dateTimeStringToLocalDateTime(endDateString);
            return criteriaBuilder.between(createDatePath, startDate, endDate);
        });
    }
}
