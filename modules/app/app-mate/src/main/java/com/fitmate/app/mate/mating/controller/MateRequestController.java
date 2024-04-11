package com.fitmate.app.mate.mating.controller;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.service.MatingRequestService;
import com.fitmate.domain.mating.mate.domain.repository.MatingReadRepository;
import com.fitmate.domain.mating.mate.dto.MatingQuestionDto;
import com.fitmate.system.security.dto.AuthDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "02-02. Mate Request", description = "메이트 신청 관리 API")
@RestController
@RequestMapping("/request/{mateId}")
@RequiredArgsConstructor
public class MateRequestController {

    private final MatingReadRepository matingReadRepository;

    private final MatingRequestService matingRequestService;

    @Operation(summary = "메이트 신청 질문 조회", description = "메이트 신청 질문 조회 API")
    @GetMapping("/question")
    public ResponseEntity<MatingQuestionDto.Response> getComeQuestion(@PathVariable Long mateId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(matingReadRepository.readQuestion(mateId));
    }

    @Operation(summary = "메이트 신청", description = "메이트 신청 API(결제기능 연동 전)")
    @PostMapping
    public ResponseEntity<?> applyMate(@PathVariable Long mateId,
                                       @RequestBody MatingDto.Apply applyDto,
                                       @AuthenticationPrincipal AuthDetails authDetails) {
        applyDto.setMatingId(mateId);
        applyDto.setAccountId(authDetails.getAccount().getId());
        matingRequestService.matingRequest(applyDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메이트 신청 승인", description = "메이트 신청 승인 API")
    @PutMapping
    public ResponseEntity<?> approveMate(@PathVariable Long mateId,
                                         @RequestBody MatingDto.Approve approveDto,
                                         @AuthenticationPrincipal AuthDetails authDetails) {
        approveDto.setMatingId(mateId);
        matingRequestService.approveRequest(approveDto, authDetails.getAccount().getId());

        return ResponseEntity.ok().build();
    }
}
