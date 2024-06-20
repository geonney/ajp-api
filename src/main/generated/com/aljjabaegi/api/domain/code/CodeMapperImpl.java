package com.aljjabaegi.api.domain.code;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.code.record.CodeSearchResponse;
import com.aljjabaegi.api.entity.Code;
import com.aljjabaegi.api.entity.enumerated.UseYn;
import com.aljjabaegi.api.entity.key.CodeKey;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-20T09:38:53+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class CodeMapperImpl implements CodeMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public CodeSearchResponse toSearchResponse(Code entity) {
        if ( entity == null ) {
            return null;
        }

        String codeGroupId = null;
        String codeId = null;
        String createDate = null;
        String modifyDate = null;
        String codeName = null;
        Integer codeOrder = null;
        UseYn useYn = null;

        codeGroupId = entityKeyCodeGroupId( entity );
        codeId = entityKeyCodeId( entity );
        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        codeName = entity.getCodeName();
        codeOrder = entity.getCodeOrder();
        useYn = entity.getUseYn();

        CodeSearchResponse codeSearchResponse = new CodeSearchResponse( codeGroupId, codeId, codeName, codeOrder, useYn, createDate, modifyDate );

        return codeSearchResponse;
    }

    @Override
    public List<CodeSearchResponse> toSearchListResponse(List<Code> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CodeSearchResponse> list = new ArrayList<CodeSearchResponse>( entityList.size() );
        for ( Code code : entityList ) {
            list.add( toSearchResponse( code ) );
        }

        return list;
    }

    private String entityKeyCodeGroupId(Code code) {
        if ( code == null ) {
            return null;
        }
        CodeKey key = code.getKey();
        if ( key == null ) {
            return null;
        }
        String codeGroupId = key.getCodeGroupId();
        if ( codeGroupId == null ) {
            return null;
        }
        return codeGroupId;
    }

    private String entityKeyCodeId(Code code) {
        if ( code == null ) {
            return null;
        }
        CodeKey key = code.getKey();
        if ( key == null ) {
            return null;
        }
        String codeId = key.getCodeId();
        if ( codeId == null ) {
            return null;
        }
        return codeId;
    }
}
