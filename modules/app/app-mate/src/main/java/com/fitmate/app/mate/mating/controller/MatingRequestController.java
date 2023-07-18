package com.fitmate.app.mate.mating.controller;

import com.fitmate.domain.mating.domain.repository.MatingReadRepository;
import com.fitmate.domain.mating.dto.MatingQuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mating/{matingId}/request")
@RequiredArgsConstructor
public class MatingRequestController {

    private final MatingReadRepository matingReadRepository;

    @GetMapping("/question")
    public ResponseEntity<MatingQuestionDto.Response> getComeQuestion(@PathVariable Long matingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(matingReadRepository.readQuestion(matingId));
    }
}
