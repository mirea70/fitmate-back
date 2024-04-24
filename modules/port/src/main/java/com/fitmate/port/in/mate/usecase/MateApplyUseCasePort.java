package com.fitmate.port.in.mate.usecase;

import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;

public interface MateApplyUseCasePort {
    MateQuestionResponse readQuestion(Long mateId);
    void applyMate(MateApplyCommand mateApplyCommand);
    void approveMate(MateApproveCommand mateApproveCommand);
}
