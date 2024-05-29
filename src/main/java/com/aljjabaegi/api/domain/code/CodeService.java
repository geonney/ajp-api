package com.aljjabaegi.api.domain.code;

import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.code.record.CodeSearchResponse;
import com.aljjabaegi.api.entity.Code;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-05-29
 */
@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;
    private final CodeMapper codeMapper = CodeMapper.INSTANCE;

    public ItemsResponse<CodeSearchResponse> getCodeList(DynamicRequest dynamicRequest) {

        List<Code> codes = codeRepository.findAll();
        return ItemsResponse.<CodeSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize((long) codes.size())
                .items(codeMapper.toSearchListResponse(codes))
                .build();
    }
}
