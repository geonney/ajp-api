package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.member.record.MemberCreateRequest;
import com.aljjabaegi.api.domain.member.record.MemberCreateResponse;
import com.aljjabaegi.api.domain.member.record.MemberModifyRequest;
import com.aljjabaegi.api.domain.member.record.MemberModifyResponse;
import com.aljjabaegi.api.domain.member.record.MemberSearchResponse;
import com.aljjabaegi.api.entity.Authority;
import com.aljjabaegi.api.entity.Member;
import com.aljjabaegi.api.entity.MemberTeam;
import com.aljjabaegi.api.entity.Team;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-24T11:07:48+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_0159776256 = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public MemberSearchResponse toSearchResponse(Member entity) {
        if ( entity == null ) {
            return null;
        }

        String teamName = null;
        String birthDate = null;
        String createDate = null;
        String modifyDate = null;
        String memberId = null;
        String memberName = null;
        String cellphone = null;

        teamName = entityTeamTeamTeamName( entity );
        if ( entity.getBirthDate() != null ) {
            birthDate = dateTimeFormatter_yyyy_MM_dd_0159776256.format( entity.getBirthDate() );
        }
        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        memberId = entity.getMemberId();
        memberName = entity.getMemberName();
        cellphone = entity.getCellphone();

        MemberSearchResponse memberSearchResponse = new MemberSearchResponse( memberId, memberName, birthDate, cellphone, teamName, createDate, modifyDate );

        return memberSearchResponse;
    }

    @Override
    public List<MemberSearchResponse> toSearchResponseList(List<Member> list) {
        if ( list == null ) {
            return null;
        }

        List<MemberSearchResponse> list1 = new ArrayList<MemberSearchResponse>( list.size() );
        for ( Member member : list ) {
            list1.add( toSearchResponse( member ) );
        }

        return list1;
    }

    @Override
    public MemberCreateResponse toCreateResponse(Member entity) {
        if ( entity == null ) {
            return null;
        }

        String teamName = null;
        String createDate = null;
        String modifyDate = null;
        String memberId = null;
        String memberName = null;
        String cellphone = null;

        teamName = entityTeamTeamTeamName( entity );
        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        memberId = entity.getMemberId();
        memberName = entity.getMemberName();
        cellphone = entity.getCellphone();

        boolean isUse = Converter.useYnToBoolean(entity.getUseYn());

        MemberCreateResponse memberCreateResponse = new MemberCreateResponse( memberId, memberName, cellphone, isUse, teamName, createDate, modifyDate );

        return memberCreateResponse;
    }

    @Override
    public MemberModifyResponse toModifyResponse(Member entity) {
        if ( entity == null ) {
            return null;
        }

        String teamName = null;
        String createDate = null;
        String modifyDate = null;
        String memberId = null;
        String memberName = null;
        String cellphone = null;

        teamName = entityTeamTeamTeamName( entity );
        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        memberId = entity.getMemberId();
        memberName = entity.getMemberName();
        cellphone = entity.getCellphone();

        boolean isUse = Converter.useYnToBoolean(entity.getUseYn());

        MemberModifyResponse memberModifyResponse = new MemberModifyResponse( memberId, memberName, teamName, cellphone, isUse, createDate, modifyDate );

        return memberModifyResponse;
    }

    @Override
    public Member toEntity(MemberCreateRequest memberCreateRequest) {
        if ( memberCreateRequest == null ) {
            return null;
        }

        Member member = new Member();

        member.setAuthority( memberCreateRequestToAuthority( memberCreateRequest ) );
        member.setMemberId( memberCreateRequest.memberId() );
        member.setMemberName( memberCreateRequest.memberName() );
        member.setCellphone( memberCreateRequest.cellphone() );

        member.setUseYn( Converter.booleanToUseYn(memberCreateRequest.isUse()) );
        member.setPassword( Converter.encodePassword(memberCreateRequest.password()) );
        member.setPasswordUpdateDate( Converter.getToday() );

        return member;
    }

    @Override
    public Member updateFromRequest(MemberModifyRequest memberModifyRequest, Member entity) {
        if ( memberModifyRequest == null ) {
            return entity;
        }

        entity.setMemberId( memberModifyRequest.memberId() );
        entity.setMemberName( memberModifyRequest.memberName() );
        entity.setCellphone( memberModifyRequest.cellphone() );

        entity.setUseYn( Converter.booleanToUseYn(memberModifyRequest.isUse()) );

        return entity;
    }

    private String entityTeamTeamTeamName(Member member) {
        if ( member == null ) {
            return null;
        }
        MemberTeam team = member.getTeam();
        if ( team == null ) {
            return null;
        }
        Team team1 = team.getTeam();
        if ( team1 == null ) {
            return null;
        }
        String teamName = team1.getTeamName();
        if ( teamName == null ) {
            return null;
        }
        return teamName;
    }

    protected Authority memberCreateRequestToAuthority(MemberCreateRequest memberCreateRequest) {
        if ( memberCreateRequest == null ) {
            return null;
        }

        Authority authority = new Authority();

        if ( memberCreateRequest.authorityCode() != null ) {
            authority.setAuthorityCode( memberCreateRequest.authorityCode() );
        }
        else {
            authority.setAuthorityCode( "ROLE_TEST" );
        }

        return authority;
    }
}
