package com.fitmate.adapter.out.persistence.jpa.mate.mapper;

import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateQuestionJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateRequestJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateRequestSimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateSimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateFeeJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateJpaEntity;
import com.fitmate.domain.mate.aggregate.Mate;
import com.fitmate.domain.mate.aggregate.MateRequest;
import com.fitmate.domain.mate.vo.*;
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
                null,
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
                .map(mateFeeJpaEntity ->  new MateFee(
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

    public MateRequestJpaEntity domainToEntity(MateRequest mateRequest) {
        return new MateRequestJpaEntity(
                mateRequest.getId() != null ? mateRequest.getId().getValue() : null,
                mateRequest.getComeAnswer(),
                mateRequest.getMateId(),
                mateRequest.getApproveStatus().name(),
                mateRequest.getApplierId(),
                mateRequest.getCreatedAt(),
                mateRequest.getUpdatedAt()
        );
    }

    public MateRequest entityToDomain(MateRequestJpaEntity mateRequestEntity) {
        return MateRequest.withId(
                new MateRequestId(mateRequestEntity.getId()),
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

    public MateRequestSimpleResponse jpaResponseToResponse(MateRequestSimpleJpaResponse jpaResponse) {

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

    public List<MateRequestSimpleResponse> jpaResponsesToResponsesForMateRequest(List<MateRequestSimpleJpaResponse> jpaResponses) {
        return jpaResponses.stream()
                .map(this::jpaResponseToResponse)
                .toList();
    }
}
