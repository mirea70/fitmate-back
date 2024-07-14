package com.fitmate.port.out.mate;

import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.in.common.SliceCommand;
import com.fitmate.port.in.mate.command.MateListCommand;
import com.fitmate.port.out.common.SliceResponse;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;

import java.util.List;

public interface LoadMatePort {
    Long saveMateEntity(Mate mate);
    void saveMateFeeEntities(List<MateFee> mateFees, MateId id);
    Mate loadMateEntity(MateId id);
    SliceResponse<MateSimpleResponse> loadMates(MateListCommand command);
    void deleteAllMateByWriter(AccountId id);
    void deleteAllMateFeeByMateId(MateId id);
}
