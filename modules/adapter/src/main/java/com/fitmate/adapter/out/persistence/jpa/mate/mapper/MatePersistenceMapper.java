package com.fitmate.adapter.out.persistence.jpa.mate.mapper;

import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateApplySimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateQuestionJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateApplyJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateSimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateFeeJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateJpaEntity;
import com.fitmate.domain.mate.*;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.apply.MateApplyId;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import com.fitmate.port.out.mate.dto.MateRequestSimpleResponse;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatePersistenceMapper {

    public MateJpaEntity domainToEntity(Mate mate) {
        Long id = mate.getId() != null ? mate.getId().getValue() : null;
        FitPlace fitPlace = mate.getFitPlace();
        PermitAges permitAges = mate.getPermitAges();

        return new MateJpaEntity(
                id,
                mate.getFitCategory().name(),
                mate.getTitle(),
                mate.getIntroduction(),
                mate.getIntroImageIds(),
                mate.getMateAt(),
                fitPlace.getName(),
                fitPlace.getAddress(),
                mate.getGatherType().name(),
                mate.getPermitGender().name(),
                permitAges.getMax(),
                permitAges.getMin(),
                mate.getPermitPeopleCnt(),
                mate.getWriterId(),
                mate.getApplyQuestion(),
                mate.getTotalFee(),
                mate.getWaitingAccountIds(),
                mate.getApprovedAccountIds(),
                mate.getCreatedAt(),
                mate.getUpdatedAt()
        );
    }

    public MateFeeJpaEntity domainToEntity(MateFee mateFee, Long mateId) {

        return new MateFeeJpaEntity(
                mateFee.getId() != null ? mateFee.getId().getValue() : null,
                mateId,
                mateFee.getName(),
                mateFee.getFee()
        );
    }

    public List<MateFeeJpaEntity> domainsToEntities(List<MateFee> mateFees, Long mateId) {
        return mateFees.stream()
                .map(mateFee -> this.domainToEntity(mateFee, mateId))
                .toList();
    }

    public Mate entityToDomain(MateJpaEntity mateEntity, List<MateFeeJpaEntity> mateFeeEntities) {

        FitPlace fitPlace = new FitPlace(
                mateEntity.getFitPlaceName(),
                mateEntity.getFitPlaceAddress()
        );

        PermitAges permitAges = new PermitAges(
                mateEntity.getPermitMaxAge(),
                mateEntity.getPermitMinAge()
        );

        List<MateFee> mateFees = mateFeeEntities.stream()
                .map(mateFeeJpaEntity ->  MateFee.withId(
                        new MateFeeId(mateFeeJpaEntity.getId()),
                        mateFeeJpaEntity.getMateId(),
                        mateFeeJpaEntity.getName(),
                        mateFeeJpaEntity.getFee()
                )).toList();

        return Mate.withId(
                new MateId(mateEntity.getId()),
                FitCategory.valueOf(mateEntity.getFitCategory()),
                mateEntity.getTitle(),
                mateEntity.getIntroduction(),
                mateEntity.getIntroImageIds(),
                mateEntity.getMateAt(),
                fitPlace,
                GatherType.valueOf(mateEntity.getGatherType()),
                PermitGender.valueOf(mateEntity.getPermitGender()),
                permitAges,
                mateEntity.getPermitPeopleCnt(),
                mateEntity.getWriterId(),
                mateFees,
                mateEntity.getApplyQuestion(),
                mateEntity.getWaitingAccountIds(),
                mateEntity.getApprovedAccountIds(),
                mateEntity.getCreatedAt(),
                mateEntity.getUpdatedAt()
        );
    }

    public MateApplyJpaEntity domainToEntity(MateApply mateApply) {
        return new MateApplyJpaEntity(
                mateApply.getId() != null ? mateApply.getId().getValue() : null,
                mateApply.getComeAnswer(),
                mateApply.getMateId(),
                mateApply.getApproveStatus().name(),
                mateApply.getApplierId(),
                mateApply.getCreatedAt(),
                mateApply.getUpdatedAt()
        );
    }

    public MateApply entityToDomain(MateApplyJpaEntity mateRequestEntity) {
        return MateApply.withId(
                new MateApplyId(mateRequestEntity.getId()),
                mateRequestEntity.getComeAnswer(),
                mateRequestEntity.getMateId(),
                mateRequestEntity.getApplierId(),
                ApproveStatus.valueOf(mateRequestEntity.getApproveStatus()),
                mateRequestEntity.getCreatedAt(),
                mateRequestEntity.getUpdatedAt()
        );
    }

    public MateSimpleResponse jpaResponseToResponse(MateSimpleJpaResponse jpaResponse) {
        return new MateSimpleResponse(
                jpaResponse.getId(),
                FitCategory.valueOf(jpaResponse.getFitCategory()),
                jpaResponse.getTitle(),
                jpaResponse.getMateAt(),
                jpaResponse.getFitPlaceName(),
                jpaResponse.getFitPlaceAddress(),
                GatherType.valueOf(jpaResponse.getGatherType()),
                PermitGender.valueOf(jpaResponse.getPermitGender()),
                jpaResponse.getPermitMaxAge(),
                jpaResponse.getPermitMinAge(),
                jpaResponse.getPermitPeopleCnt(),
                jpaResponse.getWaitingAccountIds() != null ? jpaResponse.getWaitingAccountIds().size() : 0,
                jpaResponse.getApprovedAccountIds() != null ? jpaResponse.getApprovedAccountIds().size() : 0
        );
    }

    public List<MateSimpleResponse> jpaResponsesToResponses(List<MateSimpleJpaResponse> jpaResponses) {
        return jpaResponses.stream()
                .map(this::jpaResponseToResponse)
                .toList();
    }

    public MateQuestionResponse jpaResponseToResponse(MateQuestionJpaResponse jpaResponse) {
        return new MateQuestionResponse(
                jpaResponse.getProfileImageId(),
                jpaResponse.getWriterName(),
                jpaResponse.getComeQuestion()
        );
    }

    public MateRequestSimpleResponse jpaResponseToResponse(MateApplySimpleJpaResponse jpaResponse) {

        FitPlace fitPlace = new FitPlace(
                jpaResponse.getFitPlaceName(),
                jpaResponse.getFitPlaceAddress()
        );

        return new MateRequestSimpleResponse(
                jpaResponse.getMateId(),
                jpaResponse.getThumbnailImageId(),
                jpaResponse.getTitle(),
                jpaResponse.getMateAt(),
                fitPlace,
                jpaResponse.getPermitPeopleCnt(),
                jpaResponse.getApprovedAccountCnt(),
                jpaResponse.getTotalFee(),
                jpaResponse.getApplyAt()
        );
    }

    public List<MateRequestSimpleResponse> jpaResponsesToResponsesForMateRequest(List<MateApplySimpleJpaResponse> jpaResponses) {
        return jpaResponses.stream()
                .map(this::jpaResponseToResponse)
                .toList();
    }
}
