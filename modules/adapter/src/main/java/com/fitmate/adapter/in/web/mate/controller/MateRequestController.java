package com.fitmate.adapter.in.web.mate.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.mate.dto.MateApplyRequest;
import com.fitmate.adapter.in.web.mate.dto.MateApproveRequest;
import com.fitmate.adapter.in.web.mate.mapper.MateWebAdapterMapper;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.port.in.mate.usecase.MateApplyUseCasePort;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@WebAdapter
@RestController
@RequestMapping("/api/mate/request/{mateId}")
@RequiredArgsConstructor
@Tag(name = "02-02. Mate Request", description = "메이트 신청 관리 API")
public class MateRequestController {

    private final MateApplyUseCasePort mateApplyUseCasePort;
    private final MateWebAdapterMapper mateWebAdapterMapper;


    @Operation(summary = "메이트 신청 질문 조회", description = "메이트 신청 질문 조회 API")
    @GetMapping(path = "/question")
    public ResponseEntity<MateQuestionResponse> getComeQuestion(@PathVariable Long mateId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mateApplyUseCasePort.readQuestion(mateId));
    }

    @Operation(summary = "메이트 신청", description = "메이트 신청 API(결제기능 연동 전)")
    @PutMapping(path = "/apply")
    public ResponseEntity<?> applyMate(@PathVariable Long mateId,
                                       @Valid @RequestBody MateApplyRequest applyRequest,
                                       @AuthenticationPrincipal AuthDetails authDetails) {
        mateApplyUseCasePort.applyMate(mateWebAdapterMapper.requestToCommand(
                applyRequest, mateId, authDetails.getAccount().getId()));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메이트 신청 승인", description = "메이트 신청 승인 API")
    @PutMapping("/approve")
    public ResponseEntity<?> approveMate(@PathVariable Long mateId,
                                         @Valid @RequestBody MateApproveRequest approveRequest,
                                         @AuthenticationPrincipal AuthDetails authDetails) {
        mateApplyUseCasePort.approveMate(
                mateWebAdapterMapper.requestToCommand(approveRequest, mateId, authDetails.getAccount().getId()));

        return ResponseEntity.ok().build();
    }
}
