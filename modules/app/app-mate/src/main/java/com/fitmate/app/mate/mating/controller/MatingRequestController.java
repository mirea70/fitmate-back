package com.fitmate.app.mate.mating.controller;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.service.MatingRequestService;
import com.fitmate.domain.mating.mate.domain.repository.MatingReadRepository;
import com.fitmate.domain.mating.mate.dto.MatingQuestionDto;
import com.fitmate.system.security.dto.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mating/{matingId}/request")
@RequiredArgsConstructor
public class MatingRequestController {

    private final MatingReadRepository matingReadRepository;

    private final MatingRequestService matingRequestService;

    @GetMapping("/question")
    public ResponseEntity<MatingQuestionDto.Response> getComeQuestion(@PathVariable Long matingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(matingReadRepository.readQuestion(matingId));
    }

    @PostMapping
    public ResponseEntity<?> applyMate(@PathVariable Long matingId,
                                       @RequestBody MatingDto.Apply applyDto,
                                       @AuthenticationPrincipal AuthDetails authDetails) {
        applyDto.setMatingId(matingId);
        applyDto.setAccountId(authDetails.getAccount().getId());
        matingRequestService.matingRequest(applyDto);

        return ResponseEntity.ok().build();
    }
}
