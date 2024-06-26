package com.aljjabaegi.api.domain.historyLogin;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.historyLogin.record.HistoryLoginCreateRequest;
import com.aljjabaegi.api.domain.historyLogin.record.HistoryLoginSearchResponse;
import com.aljjabaegi.api.entity.HistoryLogin;
import com.aljjabaegi.api.entity.key.HistoryLoginKey;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-26T18:38:26+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class HistoryLoginMapperImpl implements HistoryLoginMapper {

    @Override
    public HistoryLoginSearchResponse toSearchResponse(HistoryLogin entity) {
        if ( entity == null ) {
            return null;
        }

        HistoryLoginSearchResponse.HistoryLoginSearchResponseBuilder historyLoginSearchResponse = HistoryLoginSearchResponse.builder();

        historyLoginSearchResponse.memberId( entityKeyMemberId( entity ) );
        historyLoginSearchResponse.loginIp( entity.getLoginIp() );

        historyLoginSearchResponse.createDate( Converter.localDateTimeToString(entity.getKey().getCreateDate()) );

        return historyLoginSearchResponse.build();
    }

    @Override
    public List<HistoryLoginSearchResponse> toSearchResponseList(List<HistoryLogin> list) {
        if ( list == null ) {
            return null;
        }

        List<HistoryLoginSearchResponse> list1 = new ArrayList<HistoryLoginSearchResponse>( list.size() );
        for ( HistoryLogin historyLogin : list ) {
            list1.add( toSearchResponse( historyLogin ) );
        }

        return list1;
    }

    @Override
    public HistoryLogin toEntity(HistoryLoginCreateRequest historyLoginRequest) {
        if ( historyLoginRequest == null ) {
            return null;
        }

        HistoryLogin historyLogin = new HistoryLogin();

        historyLogin.setKey( historyLoginCreateRequestToHistoryLoginKey( historyLoginRequest ) );
        historyLogin.setLoginIp( historyLoginRequest.loginIp() );

        return historyLogin;
    }

    private String entityKeyMemberId(HistoryLogin historyLogin) {
        if ( historyLogin == null ) {
            return null;
        }
        HistoryLoginKey key = historyLogin.getKey();
        if ( key == null ) {
            return null;
        }
        String memberId = key.getMemberId();
        if ( memberId == null ) {
            return null;
        }
        return memberId;
    }

    protected HistoryLoginKey historyLoginCreateRequestToHistoryLoginKey(HistoryLoginCreateRequest historyLoginCreateRequest) {
        if ( historyLoginCreateRequest == null ) {
            return null;
        }

        HistoryLoginKey historyLoginKey = new HistoryLoginKey();

        historyLoginKey.setMemberId( historyLoginCreateRequest.memberId() );
        historyLoginKey.setCreateDate( historyLoginCreateRequest.createDate() );

        return historyLoginKey;
    }
}
