package com.aljjabaegi.api.domain.team;

import com.aljjabaegi.api.domain.team.record.TeamCreateRequest;
import com.aljjabaegi.api.domain.team.record.TeamCreateResponse;
import com.aljjabaegi.api.domain.team.record.TeamModifyRequest;
import com.aljjabaegi.api.domain.team.record.TeamModifyResponse;
import com.aljjabaegi.api.domain.team.record.TeamSearchResponse;
import com.aljjabaegi.api.entity.Team;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-10T07:49:19+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class TeamMapperImpl implements TeamMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public TeamSearchResponse toSearchResponse(Team entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        Long teamId = null;
        String teamName = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        teamId = entity.getTeamId();
        teamName = entity.getTeamName();

        TeamSearchResponse teamSearchResponse = new TeamSearchResponse( teamId, teamName, createDate, modifyDate );

        return teamSearchResponse;
    }

    @Override
    public List<TeamSearchResponse> toSearchResponseList(List<Team> list) {
        if ( list == null ) {
            return null;
        }

        List<TeamSearchResponse> list1 = new ArrayList<TeamSearchResponse>( list.size() );
        for ( Team team : list ) {
            list1.add( toSearchResponse( team ) );
        }

        return list1;
    }

    @Override
    public TeamCreateResponse toCreateResponse(Team entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        Long teamId = null;
        String teamName = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        teamId = entity.getTeamId();
        teamName = entity.getTeamName();

        TeamCreateResponse teamCreateResponse = new TeamCreateResponse( teamId, teamName, createDate, modifyDate );

        return teamCreateResponse;
    }

    @Override
    public TeamModifyResponse toModifyResponse(Team entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        Long teamId = null;
        String teamName = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        teamId = entity.getTeamId();
        teamName = entity.getTeamName();

        TeamModifyResponse teamModifyResponse = new TeamModifyResponse( teamId, teamName, createDate, modifyDate );

        return teamModifyResponse;
    }

    @Override
    public Team toEntity(TeamCreateRequest teamCreateRequest) {
        if ( teamCreateRequest == null ) {
            return null;
        }

        Team team = new Team();

        team.setTeamName( teamCreateRequest.teamName() );

        return team;
    }

    @Override
    public Team updateFromRequest(TeamModifyRequest teamModifyRequest, Team entity) {
        if ( teamModifyRequest == null ) {
            return entity;
        }

        entity.setTeamId( teamModifyRequest.teamId() );
        entity.setTeamName( teamModifyRequest.teamName() );

        return entity;
    }
}
