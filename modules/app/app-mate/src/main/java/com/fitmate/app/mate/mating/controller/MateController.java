package com.fitmate.app.mate.mating.controller;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.app.mate.mating.service.MatingRegisterService;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingReadRepository;
import com.fitmate.domain.mating.mate.dto.MatingReadResponseDto;
import com.fitmate.domain.mating.mate.service.MatingService;
import com.fitmate.system.security.dto.AuthDetails;
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

@Tag(name = "02-01. Mate", description = "메이트 글 관리 API")
@RestController
@RequiredArgsConstructor
public class MateController {
    private final MatingRegisterService matingRegisterService;
    private final MatingService matingService;
    private final MatingReadRepository matingReadRepository;

    @Operation(summary = "메이팅 글 작성", description = "메이팅 글 작성 API")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MatingDto.Response> register(@Valid @RequestPart MatingDto.Create createDto,
                                                       @Parameter(description = "소개 이미지 (여러개)")
                                                       @RequestPart(required = false) List<MultipartFile> multipartFiles,
                                                       @AuthenticationPrincipal AuthDetails authDetails) throws Exception {
        createDto.setIntroImages(multipartFiles);
        createDto.setWriterId(authDetails.getAccount().getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matingRegisterService.register(createDto));
    }

    @Operation(summary = "메이팅 글 단일조회", description = "메이팅 글 단일조회 API")
    @GetMapping("/{mateId}")
    public ResponseEntity<MatingDto.Response> findOne(@PathVariable Long mateId) {
        Mating findMating = matingService.validateFindById(mateId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MatingDtoMapper.INSTANCE.toResponse(findMating));
    }

    @Operation(summary = "메이팅 글 목록 조회", description = "메이팅 글 목록 조회 API : 무한 스크롤")
    @GetMapping
    public ResponseEntity<List<MatingReadResponseDto>> findList(@RequestBody MatingDto.ListRequest listDto) {
        List<MatingReadResponseDto> responses = matingReadRepository.readList(listDto.getLastMatingId(), listDto.getLimit());
        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }
}
