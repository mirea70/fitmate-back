package com.fitmate.port.out.mate;

import com.fitmate.domain.account.vo.AccountId;
import com.fitmate.domain.mate.aggregate.MateRequest;
import com.fitmate.domain.mate.vo.ApproveStatus;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import com.fitmate.port.out.mate.dto.MateRequestSimpleResponse;

import java.util.List;

public interface LoadMateRequestPort {
    MateQuestionResponse loadMateQuestion(Long mateId);
    void isDuplicateMateRequest(Long mateId, Long accountId);
    void saveMateRequestEntity(MateRequest mateRequest);
    MateRequest loadMateRequestEntity(Long mateId, Long applierId);
    List<MateRequestSimpleResponse> loadMateRequests(Long applierId, ApproveStatus approveStatus);
    void deleteAllMateRequestByApplier(AccountId id);
}
