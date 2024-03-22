package com.fitmate.app.mate.account.controller;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountExtraController {

    private final MatingRepository matingRepository;

    // 나의 메이트 작성 목록 조회 기능
    @GetMapping("/{accountId}/my/mate/register/list")
    public ResponseEntity<List<MatingDto.MyMateResponse>> getMyMateRegisterList(@PathVariable Long accountId) {
        List<Mating> matings = matingRepository.findAllByWriterIdOrderByCreatedAtDesc(accountId);
        return ResponseEntity.ok(MatingDtoMapper.INSTANCE.toMyMateListResponses(matings));
    }

    // 나의 메이트 신청 목록 조회 기능
    @GetMapping("/{accountId}/my/mate/request/list")
    public ResponseEntity<?> getMyMateRequestList(@PathVariable Long accountId) {
        return null;
    }
}
