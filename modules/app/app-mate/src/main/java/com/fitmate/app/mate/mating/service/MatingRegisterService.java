package com.fitmate.app.mate.mating.service;

import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MatingRegisterService {
    private final MatingRepository matingRepository;
    private final AccountService accountService;
    private final FileService fileService;

    public MatingDto.Response register(MatingDto.Create request) throws IOException {
        Mating readyMating = setRegisterEntity(request);
        Mating savedMating = matingRepository.save(readyMating);

        return getResponse(savedMating, request.getWriterId());
    }

    private Mating setRegisterEntity(MatingDto.Create request) throws IOException {

        Set<Long> imageIds = null;
        if(request.getIntroImages() != null && !request.getIntroImages().isEmpty()) {
            imageIds = fileService.uploadFiles(request.getIntroImages())
                    .stream().map((AttachFileDto.Response::getId)).collect(Collectors.toSet());
        }
        Mating mating = MatingDtoMapper.INSTANCE.toEntity(request);
        mating.updateIntroImages(imageIds);

        return mating;
    }

    private MatingDto.Response getResponse(Mating savedMating, Long writerId) {
        MatingDto.Response response = MatingDtoMapper.INSTANCE.toResponse(savedMating);
        AccountDataDto.Response accountData = accountService.validateFindById(writerId);
        response.setWriterNickName(accountData.getProfileInfo().getNickName());

        return response;
    }
}
