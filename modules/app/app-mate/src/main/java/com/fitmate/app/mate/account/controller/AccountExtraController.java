package com.fitmate.app.mate.account.controller;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.app.mate.notice.dto.NoticeDto;
import com.fitmate.app.mate.notice.mapper.NoticeDtoMapper;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mating.mate.dto.MyMateRequestsDto;
import com.fitmate.domain.mating.mate.service.MatingService;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.redis.entity.Notice;
import com.fitmate.domain.redis.repository.NoticeRepository;
import com.fitmate.system.security.dto.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountExtraController {

    private final MatingService matingService;
    private final MatingRepository matingRepository;
    private final NoticeRepository noticeRepository;

    @GetMapping("/{accountId}/my/mate/register/list")
    public ResponseEntity<List<MatingDto.MyMateResponse>> getMyMateRegisterList(@PathVariable Long accountId) {
        List<Mating> matings = matingRepository.findAllByWriterIdOrderByCreatedAtDesc(accountId);
        return ResponseEntity.ok(MatingDtoMapper.INSTANCE.toMyMateListResponses(matings));
    }

    @GetMapping("/{accountId}/my/mate/request/list")
    public ResponseEntity<List<MyMateRequestsDto>> getMyMateRequestList(@PathVariable Long accountId,
                                                                        @RequestParam MateRequest.ApproveStatus approveStatus) {
        return ResponseEntity.ok(matingService.findAllMyMateRequest(accountId, approveStatus));
    }

    @GetMapping("/my/notice/list")
    public ResponseEntity<List<NoticeDto.Response>> getMyNoticeList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<Notice> notices = noticeRepository.findAllByAccountIdOrderByCreatedAtDesc(authDetails.getAccount().getId());
        return ResponseEntity.ok(NoticeDtoMapper.INSTANCE.toResponses(notices));
    }
}
