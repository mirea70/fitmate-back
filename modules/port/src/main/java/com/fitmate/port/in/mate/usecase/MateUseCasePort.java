package com.fitmate.port.in.mate.usecase;

import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.in.mate.command.MateModifyCommand;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;

import java.util.List;

public interface MateUseCasePort {
    void registerMate(MateCreateCommand command);
    MateDetailResponse findMate(Long mateId);
    List<MateSimpleResponse> findAllMate(Long lastMatingId, Integer limit);
    void modifyMate(MateModifyCommand command);
}
