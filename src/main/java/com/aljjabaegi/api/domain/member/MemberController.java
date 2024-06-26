package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.response.GridResponse;
import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.common.validator.DynamicValid;
import com.aljjabaegi.api.domain.member.record.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 Controller
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-02 GEONLEE - 응답 객체 Builder Pattern 적용<br />
 * 2024-04-04 GEONLEE - Member Operation 전체 인증 적용
 */
@RestController
@Tag(name = "02. Member Management [Search using DynamicSpecification, Query method]", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@Validated
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/v1/members-dynamic-filter")
//    @Secured("ROLE_ADMIN")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            examples = {
                    @ExampleObject(name = "eq (equal)", value = """
                                    [
                                        {
                                            "field":"memberId",
                                            "operator":"eq",
                                            "value":"honggildong123"
                                        }
                                    ]
                            """),
                    @ExampleObject(name = "neq (notEqual)", value = """
                                    [
                                        {
                                            "field":"memberId",
                                            "operator":"neq",
                                            "value":"honggildong123"
                                        }
                                    ]
                            """),
                    @ExampleObject(name = "contains (like)", value = """
                                    [
                                        {
                                            "field":"memberName",
                                            "operator":"contains",
                                            "value":"길동"
                                        }
                                    ]
                            """),
                    @ExampleObject(name = "between (between)", value = """
                                    [
                                        {
                                            "field":"createDate",
                                            "operator":"between",
                                            "value":"20240408182256,20240408235959"
                                        }
                                    ]
                            """),
                    @ExampleObject(name = "in (in)", value = """
                                    [
                                        {
                                            "field":"birthDate",
                                            "operator":"in",
                                            "value":"19900305,19860107"
                                        }
                                    ]
                            """),
                    @ExampleObject(name = "lte (less than equal)", value = """
                                    [
                                        {
                                            "field":"age",
                                            "operator":"lte",
                                            "value":"20"
                                        }
                                    ]
                            """),
                    @ExampleObject(name = "gte (greater than equal)", value = """
                                    [
                                        {
                                            "field":"birthDate",
                                            "operator":"gte",
                                            "value":"19860107"
                                        }
                                    ]
                            """),
                    @ExampleObject(name = "Reference entity field", value = """
                                    [
                                        {
                                            "field":"team.teamName",
                                            "operator":"contains",
                                            "value":"명1"
                                        }
                                    ]
                            """),
                    @ExampleObject(name = "Not searchable field", value = """
                                    [
                                        {
                                            "field":"password",
                                            "operator":"contains",
                                            "value":"pass"
                                        }
                                    ]
                            """)
            }
    ))
    @Operation(summary = "Search members (DynamicSpecification)", operationId = "API-MEMBER-01", description = """
            Searchable Field
             - memberId
             - memberName
             - cellphone
             - birthDate
             - age
             - useYn ('Y', 'N')
             - createDate
             - updateDate
             - team.teamName (Referenced entity field)
             
            Operators (See examples)
             - eq (equal)
             - neq (notEqual)
             - contains (like)
             - between (between)
             - in (in)
             - lte (less then equal)
             - gte (greater than equal)
            """)
    public ResponseEntity<ItemsResponse<MemberSearchResponse>> getMemberList(
            @RequestBody @DynamicValid(essentialFields = {"memberId : 사용자명", "memberName:사용자명 "}) DynamicRequest dynamicRequest) {
        List<MemberSearchResponse> memberSearchResponseList = memberService.getMemberList(dynamicRequest.filter());
        long size = memberSearchResponseList.size();
        return ResponseEntity.ok()
                .body(ItemsResponse.<MemberSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .totalSize(size)
                        .items(memberSearchResponseList).build());
    }

    @PostMapping(value = "/v1/members-dynamic-request")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            examples = {
                    @ExampleObject(name = "filtering and sorting with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "filter": [
                                            {
                                                "field":"memberName",
                                                "operator":"contains",
                                                "value":"홍"
                                            }
                                        ],
                                        "sorter": [
                                            {
                                                "field":"createDate",
                                                "direction":"DESC"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "filtering with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "filter": [
                                            {
                                                "field":"memberName",
                                                "operator":"contains",
                                                "value":"홍"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "sorting with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "sorter": [
                                            {
                                                "field":"createDate",
                                                "direction":"DESC"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "Empty parameter", value = """
                                    {
                                        
                                    }
                            """),
            }
    ))
    @Operation(summary = "Search members (DynamicSpecification + paging + sorting)", operationId = "API-MEMBER-02")
    public ResponseEntity<GridResponse<MemberSearchResponse>> getUserListUsingDynamicRequest(@RequestBody DynamicRequest dynamicRequest) {
        GridResponse<MemberSearchResponse> gridItemsResponse =
                memberService.getUserListUsingDynamicRequest(dynamicRequest);
        return ResponseEntity.ok()
                .body(gridItemsResponse);
    }

    @GetMapping(value = "/v1/members/{memberId}")
    @Operation(summary = "Search member (Query method)", operationId = "API-MEMBER-03")
    public ResponseEntity<ItemResponse<MemberSearchResponse>> getMembers(@PathVariable String memberId) {
        MemberSearchResponse userSearchResponse = memberService.getMembers(memberId);
        return ResponseEntity.ok()
                .body(ItemResponse.<MemberSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .item(userSearchResponse).build());
    }

    @GetMapping(value = "/v1/check/member-id/{memberId}")
    @Operation(summary = "Check for ID duplicates", operationId = "API-MEMBER-04")
    public ResponseEntity<ItemResponse<Boolean>> checkMemberId(@PathVariable String memberId) {
        boolean isDuplication = memberService.checkMemberId(memberId);
        String message = (isDuplication) ? "중복된 ID가 존재합니다." : "사용 가능한 ID 입니다.";
        return ResponseEntity.ok()
                .body(ItemResponse.<Boolean>builder()
                        .status("OK")
                        .message(message)
                        .item(isDuplication).build());
    }

    @PostMapping(value = "/v1/member")
    @Operation(summary = "Create Member", operationId = "API-MEMBER-05")
    public ResponseEntity<ItemResponse<MemberCreateResponse>> createMember(@RequestBody @Valid MemberCreateRequest parameter) {
        MemberCreateResponse createdMember = memberService.createMember(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<MemberCreateResponse>builder()
                        .status("OK")
                        .message("데이터를 추가하는데 성공하였습니다.")
                        .item(createdMember).build());
    }

    @PutMapping(value = "/v1/member")
    @Operation(summary = "Modify Member", operationId = "API-MEMBER-06")
    public ResponseEntity<ItemResponse<MemberModifyResponse>> modifyUser(
            @RequestBody @Valid MemberModifyRequest parameter, HttpServletResponse httpServletResponse) {
        MemberModifyResponse modifiedMember = memberService.modifyMember(parameter, httpServletResponse);
        return ResponseEntity.ok()
                .body(ItemResponse.<MemberModifyResponse>builder()
                        .status("OK")
                        .message("데이터를 수정하는데 성공하였습니다.")
                        .item(modifiedMember).build());
    }

    @DeleteMapping(value = "/v1/member/{memberId}")
    @Operation(summary = "Delete Member", operationId = "API-MEMBER-07")
    public ResponseEntity<ItemResponse<Long>> deleteMember(@PathVariable String memberId) {
        Long deleteCount = memberService.deleteMember(memberId);
        return ResponseEntity.ok()
                .body(ItemResponse.<Long>builder()
                        .status("OK")
                        .message("데이터를 삭제하는데 성공하였습니다.")
                        .item(deleteCount).build());
    }
}
