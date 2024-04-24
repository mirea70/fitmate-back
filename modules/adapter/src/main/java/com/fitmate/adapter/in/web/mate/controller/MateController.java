package com.fitmate.adapter.in.web.mate.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.mate.dto.MateCreateRequest;
import com.fitmate.adapter.in.web.mate.mapper.MateWebAdapterMapper;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.port.in.mate.usecase.MateUseCasePort;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/mate")
@RequiredArgsConstructor
@Tag(name = "02-01. Mate", description = "메이트 관리 API")
public class MateController {

    private final MateUseCasePort mateUseCasePort;
    private final MateWebAdapterMapper mateWebAdapterMapper;

    @Operation(summary = "메이팅 글 작성", description = "메이팅 글 작성 API")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody MateCreateRequest createRequest,
                                      @AuthenticationPrincipal AuthDetails authDetails) throws Exception {
        mateUseCasePort.registerMate(mateWebAdapterMapper.requestToCommand(createRequest, authDetails.getAccount().getId()));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메이팅 글 단일조회", description = "메이팅 글 단일조회 API")
    @GetMapping("/{mateId}")
    public ResponseEntity<MateDetailResponse> findOne(@PathVariable Long mateId) {
        MateDetailResponse response = mateUseCasePort.findMate(mateId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "메이팅 글 목록 조회", description = "메이팅 글 목록 조회 API : 무한 스크롤")
    @GetMapping
    public ResponseEntity<List<MateSimpleResponse>> findList(@Parameter(description = "최하단의 메이팅 식별 ID 값 : 이후 메이팅 글들이 추가노출됨", example = "2")
                                                             @RequestParam Long lastMatingId,
                                                             @Parameter(description = "한번에 가져올 메이팅 글의 수", example = "10")
                                                             @RequestParam Integer limit) {
        List<MateSimpleResponse> responses = mateUseCasePort.findAllMate(lastMatingId, limit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }
}
