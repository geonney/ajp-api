package com.aljjabaegi.api.domain.team;

import com.aljjabaegi.api.domain.member.MemberRepository;
import com.aljjabaegi.api.domain.team.record.*;
import com.aljjabaegi.api.entity.Team;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Team Service
 *
 * @author GEONLEE
 * @since 2024-04-08<br />
 */
@Service
@RequiredArgsConstructor
public class TeamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMapper teamMapper = TeamMapper.INSTANCE;

    /**
     * 전체 Team 조회
     *
     * @author GEONLEE
     * @since 2024-04-08
     */
    public List<TeamSearchResponse> getTeamList() {
        return teamMapper.toSearchResponseList(teamRepository.findAll());
    }

    /**
     * Team 추가
     *
     * @author GEONLEE
     * @since 2024-04-08
     */
    public TeamCreateResponse createTeam(TeamCreateRequest parameter) {
        Team createRequestEntity = teamMapper.toEntity(parameter);
        Team createdEntity = teamRepository.save(createRequestEntity);
        return teamMapper.toCreateResponse(createdEntity);
    }

    /**
     * Team 수정
     *
     * @author GEONLEE
     * @since 2024-04-08
     */
    public TeamModifyResponse modifyTeam(TeamModifyRequest parameter) {
        Team entity = teamRepository.findById(parameter.teamId())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(parameter.teamId())));
        Team modifiedEntity = teamMapper.updateFromRequest(parameter, entity);
        modifiedEntity = teamRepository.saveAndFlush(modifiedEntity);
        return teamMapper.toModifyResponse(modifiedEntity);
    }

    /**
     * Team 삭제
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    @Transactional
    public Long deleteTeam(Long teamId) {
        Team entity = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(teamId)));
        int updateSize = memberRepository.updateTeam(entity.getTeamId());
        LOGGER.info("update member size : {}", updateSize);
        teamRepository.delete(entity);
        return 1L;
    }
}
