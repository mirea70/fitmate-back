package com.fitmate.app.mate.account.controller;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.account.service.AccountProfileService;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.app.mate.notice.dto.NoticeDto;
import com.fitmate.app.mate.notice.mapper.NoticeDtoMapper;
import com.fitmate.domain.account.dto.FollowDetailDto;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mating.mate.dto.MyMateRequestsDto;
import com.fitmate.domain.mating.mate.service.MatingService;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.redis.entity.Notice;
import com.fitmate.domain.redis.repository.NoticeRepository;
import com.fitmate.system.security.dto.AuthDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.List;

@Tag(name = "01-02. Account Profile", description = "회원 프로필 관련 API")
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountProfileController {

    private final MatingService matingService;
    private final MatingRepository matingRepository;
    private final NoticeRepository noticeRepository;
    private final AccountProfileService accountProfileService;

    @Operation(summary = "회원 프로필 이미지 다운로드", description = "회원 프로필 이미지 다운로드 API", hidden = true)
    @GetMapping("/{accountId}/image")
    public ResponseEntity<UrlResource> downloadProfileImage(@Parameter(description = "요청 회원 ID")
                                                            @PathVariable final Long accountId) throws MalformedURLException {
        AttachFileDto.Download downloadDto = accountProfileService.downloadProfileImage(accountId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, downloadDto.getContentDisposition())
                .body(downloadDto.getUrlResource());
    }

    @Operation(summary = "회원 프로필 변경", description = "회원 프로필 변경 API")
    @PutMapping(value = "/{accountId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AccountDto.Response> updateProfile(@Valid @RequestPart AccountDto.UpdateRequest updateRequest,
                                                             @Parameter(description = "요청 프로필 이미지 파일")
                                                             @RequestPart(required = false) MultipartFile profileImage,
                                                             @Parameter(description = "요청 회원 ID")
                                                             @PathVariable final Long accountId) throws Exception {
        updateRequest.setAccountId(accountId);
        updateRequest.setProfileImage(profileImage);
        return ResponseEntity.ok(accountProfileService.modifyProfile(updateRequest));
    }

    @Operation(summary = "내 메이트 글 목록 조회", description = "내 메이트 글 목록 조회 API")
    @GetMapping("/my/mate/register/list")
    public ResponseEntity<List<MatingDto.MyMateResponse>> getMyMateRegisterList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<Mating> matings = matingRepository.findAllByWriterIdOrderByCreatedAtDesc(authDetails.getAccount().getId());
        return ResponseEntity.ok(MatingDtoMapper.INSTANCE.toMyMateListResponses(matings));
    }

    @Operation(summary = "내 메이트 신청 목록 조회 API", description = "파라미터인 승인상태에 따라 메이트 신청 목록을 조회해온다.")
    @GetMapping("/my/mate/request/list")
    public ResponseEntity<List<MyMateRequestsDto>> getMyMateRequestList(@AuthenticationPrincipal AuthDetails authDetails,
                                                                        @Parameter(description = "메이트 신청 목록 조회 시, 승인 상태 조건")
                                                                        @RequestParam MateRequest.ApproveStatus approveStatus) {
        return ResponseEntity.ok(matingService.findAllMyMateRequest(authDetails.getAccount().getId(), approveStatus));
    }

    @Operation(summary = "내 알림 목록 조회 API", description = "알림 목록 조회 API")
    @GetMapping("/my/notice/list")
    public ResponseEntity<List<NoticeDto.Response>> getMyNoticeList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<Notice> notices = noticeRepository.findAllByAccountIdOrderByCreatedAtDesc(authDetails.getAccount().getId());
        return ResponseEntity.ok(NoticeDtoMapper.INSTANCE.toResponses(notices));
    }

    @Operation(summary = "타 회원 팔로우 API", description = "대상이 팔로우 상태가 아니면 팔로우, 이미 팔로우 상태면 팔로우 취소한다.")
    @PutMapping("/follow")
    public ResponseEntity<?> followOrCancel(@AuthenticationPrincipal AuthDetails authDetails,
                                            @RequestParam Long targetAccountId) {
        accountProfileService.followOrCancel(authDetails.getAccount().getId(), targetAccountId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "나의 팔로잉 목록 조회", description = "나의 팔로잉 목록 조회 API")
    @GetMapping("/following")
    public ResponseEntity<List<FollowDetailDto>> getFollowingList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<FollowDetailDto> responses = accountProfileService.getFollowingList(authDetails.getAccount().getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "나의 팔로워 목록 조회", description = "나의 팔로워 목록 조회 API")
    @GetMapping("/follower")
    public ResponseEntity<List<FollowDetailDto>> getFollowerList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<FollowDetailDto> responses = accountProfileService.getFollowerList(authDetails.getAccount().getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 API")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> delete(@Parameter(description = "요청 회원 ID")
                                    @PathVariable final Long accountId) {
        accountProfileService.remove(accountId);
        return ResponseEntity.ok().build();
    }
}
