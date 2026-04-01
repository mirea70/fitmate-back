package com.fitmate.adapter.in.web.mate.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.port.in.mate.usecase.MateWishUseCasePort;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@WebAdapter
@RestController
@RequestMapping("/api/mate/wish")
@RequiredArgsConstructor
@Tag(name = "02-03. Mate Wish", description = "메이트 찜 관리 API")
public class MateWishController {

    private final MateWishUseCasePort mateWishUseCasePort;

    @Operation(summary = "메이트 찜 토글", description = "찜 상태가 아니면 찜 추가, 이미 찜 상태면 찜 해제")
    @PutMapping("/{mateId}")
    public ResponseEntity<Map<String, Boolean>> toggleWish(@PathVariable Long mateId,
                                                           @AuthenticationPrincipal AuthDetails authDetails) {
        boolean wished = mateWishUseCasePort.toggleWish(authDetails.getAccount().getId(), mateId);
        return ResponseEntity.ok(Map.of("wished", wished));
    }

    @Operation(summary = "나의 찜 목록 조회", description = "로그인한 사용자의 메이트 찜 목록 조회 API")
    @GetMapping("/my")
    public ResponseEntity<List<MateSimpleResponse>> getMyWishMates(@AuthenticationPrincipal AuthDetails authDetails) {
        List<MateSimpleResponse> responses = mateWishUseCasePort.getMyWishMates(authDetails.getAccount().getId());
        return ResponseEntity.ok(responses);
    }
}
