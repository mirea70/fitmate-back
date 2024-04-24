package com.fitmate.adapter.in.web.account.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.account.dto.AccountModifyRequest;
import com.fitmate.adapter.in.web.account.mapper.AccountWebAdapterMapper;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.domain.mate.vo.ApproveStatus;
import com.fitmate.port.in.account.usecase.AccountProfileUseCasePort;
import com.fitmate.port.out.follow.FollowDetailResponse;
import com.fitmate.port.out.mate.dto.MateRequestSimpleResponse;
import com.fitmate.port.out.notice.NoticeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/account/profile")
@RequiredArgsConstructor
@Tag(name = "01-02. Account Profile", description = "회원 프로필 관련 API")
public class AccountProfileController {
    private final AccountProfileUseCasePort accountProfileUseCasePort;
    private final AccountWebAdapterMapper accountWebAdapterMapper;

    @Operation(summary = "회원 프로필 변경", description = "회원 프로필 변경 API")
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifyProfile(@Valid @RequestBody AccountModifyRequest modifyRequest,
                                           @AuthenticationPrincipal AuthDetails authDetails) throws Exception {
        accountProfileUseCasePort.modify(accountWebAdapterMapper.requestToCommand(authDetails.getAccount().getId(), modifyRequest));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "타 회원 팔로우 API", description = "대상이 팔로우 상태가 아니면 팔로우, 이미 팔로우 상태면 팔로우 취소한다.")
    @PutMapping("/follow")
    public ResponseEntity<?> followOrCancel(@AuthenticationPrincipal AuthDetails authDetails,
                                            @RequestParam Long targetAccountId) {
        accountProfileUseCasePort.followOrCancel(authDetails.getAccount().getId(), targetAccountId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "나의 팔로잉 목록 조회", description = "나의 팔로잉 목록 조회 API")
    @GetMapping("/my/followings")
    public ResponseEntity<List<FollowDetailResponse>> getFollowingList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<FollowDetailResponse> responses = accountProfileUseCasePort.getFollowingList(authDetails.getAccount().getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "나의 팔로워 목록 조회", description = "나의 팔로워 목록 조회 API")
    @GetMapping("/my/followers")
    public ResponseEntity<List<FollowDetailResponse>> getFollowerList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<FollowDetailResponse> responses = accountProfileUseCasePort.getFollowerList(authDetails.getAccount().getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "내 알림 목록 조회 API", description = "알림 목록 조회 API")
    @GetMapping("/my/notices")
    public ResponseEntity<List<NoticeResponse>> getMyNoticeList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<NoticeResponse> responses = accountProfileUseCasePort.getNotices(authDetails.getAccount().getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "내 메이트 신청 목록 조회 API", description = "파라미터인 승인상태에 따라 메이트 신청 목록을 조회해온다.")
    @GetMapping("/my/mate/request")
    public ResponseEntity<List<MateRequestSimpleResponse>> getMyMateRequestList(@AuthenticationPrincipal AuthDetails authDetails,
                                                                                @Parameter(description = "메이트 신청 목록 조회 시, 승인 상태 조건")
                                                                        @RequestParam ApproveStatus approveStatus) {
        List<MateRequestSimpleResponse> responses = accountProfileUseCasePort.getMyMateRequests(authDetails.getAccount().getId(), approveStatus);
        return ResponseEntity.ok(responses);
    }
}
