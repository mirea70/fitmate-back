package com.fitmate.app.mate.mating.controller;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.app.mate.mating.service.MatingRegisterService;
import com.fitmate.domain.mating.domain.entity.Mating;
import com.fitmate.domain.mating.service.MatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/mating")
@RequiredArgsConstructor
public class MatingController {
    private final MatingRegisterService matingRegisterService;
    private final MatingService matingService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MatingDto.Response> register(@RequestPart MatingDto.Create createDto,
                                                       @RequestPart(required = false) List<MultipartFile> multipartFiles) throws Exception {
        createDto.setIntroImages(multipartFiles);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matingRegisterService.register(createDto));
    }

    @GetMapping("/{matingId}")
    public ResponseEntity<MatingDto.Response> findOne(@PathVariable Long matingId) {
        Mating findMating = matingService.validateFindById(matingId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MatingDtoMapper.INSTANCE.toResponse(findMating));
    }
}
