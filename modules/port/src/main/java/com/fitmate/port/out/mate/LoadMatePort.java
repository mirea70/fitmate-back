package com.fitmate.port.out.mate;

import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;

import java.util.List;

public interface LoadMatePort {
    Long saveMateEntity(Mate mate);
    void saveMateFeeEntities(List<MateFee> mateFees, MateId id);
    Mate loadMateEntity(MateId id);
    List<MateSimpleResponse> loadMates(Long lastMatingId, Integer limit);
    void deleteAllMateByWriter(AccountId id);
    void deleteAllMateFeeByMateId(MateId id);
}
