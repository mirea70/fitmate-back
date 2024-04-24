package com.fitmate.port.out.mate;

import com.fitmate.domain.account.vo.AccountId;
import com.fitmate.domain.mate.aggregate.Mate;
import com.fitmate.domain.mate.vo.MateFee;
import com.fitmate.domain.mate.vo.MateId;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;

import java.util.List;

public interface LoadMatePort {
    Long saveMateEntity(Mate mate);
    void saveMateFeeEntities(List<MateFee> mateFees, Long mateId);
    Mate loadMateEntity(MateId id);
    List<MateSimpleResponse> loadMates(Long lastMatingId, Integer limit);
    void deleteAllMateByWriter(AccountId id);
}
