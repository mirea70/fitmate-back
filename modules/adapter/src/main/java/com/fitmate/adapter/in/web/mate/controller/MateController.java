package com.fitmate.adapter.in.web.mate.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.mate.dto.MateCreateRequest;
import com.fitmate.adapter.in.web.mate.dto.MateModifyRequest;
import com.fitmate.adapter.in.web.mate.mapper.MateWebAdapterMapper;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.out.persistence.jpa.file.adapter.AttachFilePersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileResponse;
import com.fitmate.domain.error.exceptions.LimitException;
import com.fitmate.domain.error.results.LimitErrorResult;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebAdapter
@RestController
@RequestMapping("/api/mate")
@RequiredArgsConstructor
@Tag(name = "02-01. Mate", description = "메이트 관리 API")
public class MateController {

    private final MateUseCasePort mateUseCasePort;
    private final MateWebAdapterMapper mateWebAdapterMapper;
    private final AttachFilePersistenceAdapter filePersistenceAdapter;

    @Operation(summary = "메이트 글 작성", description = "Request body의 설명은 application/json 타입으로 봐주시고 테스트는 multipart/form-data 타입으로 실행해주세요.")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> register(@Valid @RequestPart MateCreateRequest createRequest,
                                      @RequestPart(required = false)
                                      @Parameter(description = "소개 이미지 파일(최대 3개)")
                                      List<MultipartFile> introImages,
                                      @AuthenticationPrincipal AuthDetails authDetails) throws Exception {
        if(introImages != null && introImages.size() > 3) throw new LimitException(LimitErrorResult.OVER_PERMIT_FILE_COUNT);
        List<FileResponse> fileResponses = filePersistenceAdapter.uploadFiles(introImages);
        Set<Long> introImageIds = null;
        if(fileResponses != null)
            introImageIds = fileResponses.stream().map(FileResponse::getAttachFileId).collect(Collectors.toSet());
        mateUseCasePort.registerMate(mateWebAdapterMapper.requestToCommand(createRequest, authDetails.getAccount().getId(), introImageIds));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메이트 글 단일조회", description = "메이트 글 단일조회 API")
    @GetMapping("/{mateId}")
    public ResponseEntity<MateDetailResponse> findOne(@PathVariable Long mateId) {
        MateDetailResponse response = mateUseCasePort.findMate(mateId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "메이팅 글 목록 조회", description = "메이팅 글 목록 조회 API : 무한 스크롤")
    @GetMapping
    public ResponseEntity<List<MateSimpleResponse>> findList(@Parameter(description = "최하단의 메이팅 식별 ID 값 : 이후 메이팅 글들이 추가노출됨", example = "2")
                                                             @RequestParam Long lastMateId,
                                                             @Parameter(description = "한번에 가져올 메이팅 글의 수", example = "10")
                                                             @RequestParam Integer limit) {
        List<MateSimpleResponse> responses = mateUseCasePort.findAllMate(lastMateId, limit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }

    @Operation(summary = "메이트 글 수정", description = """
            요청 Body에는 수정 하고싶은 내용만 포함시키면됩니다.
            
            <U>단, introImageIds 및 참여비 리스트의 경우 기존 값을 함께 포함시켜야 합니다.</U>
            
            **[참고]** : profileImageId를 입력하려면 파일 관리 API를 통한 파일 업로드를 선행해주세요.
            """)
    @PatchMapping(path = "/{mateId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@PathVariable Long mateId,
                                      @Valid @RequestBody MateModifyRequest modifyRequest,
                                      @AuthenticationPrincipal AuthDetails authDetails) throws Exception {
        mateUseCasePort.modifyMate(mateWebAdapterMapper.requestToCommand(mateId, modifyRequest, authDetails.getAccount().getId()));
        return ResponseEntity.ok().build();
    }
}
