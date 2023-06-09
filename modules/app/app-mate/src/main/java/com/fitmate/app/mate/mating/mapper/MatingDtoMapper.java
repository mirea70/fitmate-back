package com.fitmate.app.mate.mating.mapper;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.vo.EntryFeeDataInfo;
import com.fitmate.domain.mating.domain.entity.Mating;
import com.fitmate.domain.mating.domain.enums.GatherFee;
import com.fitmate.domain.mating.domain.enums.OperateFee;
import com.fitmate.domain.mating.domain.vo.EntryFeeInfo;
import com.fitmate.domain.mating.domain.vo.GatherFeeSet;
import com.fitmate.domain.mating.domain.vo.OperateFeeSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring")
public interface MatingDtoMapper {
    MatingDtoMapper INSTANCE = Mappers.getMapper(MatingDtoMapper.class);

    @Mapping(target = "introImages", ignore = true)
    @Mapping(target = "entryFeeInfo", source = "entryFeeInfo", qualifiedByName = "convertToEntryFeeInfo")
    Mating toEntity(MatingDto.Create request);

    @Mapping(target = "writerNickName", ignore = true)
    @Mapping(target = "introImages", source = "introImages", qualifiedByName = "setConvertToList")
    @Mapping(target = "entryFeeInfo", source = "entryFeeInfo", qualifiedByName = "convertToEntryDataInfo")
    MatingDto.Response toResponse(Mating mating);

    @Named("convertToEntryFeeInfo")
    default EntryFeeInfo convertToEntryFeeInfo(EntryFeeDataInfo entryFeeDataInfo) {

        GatherFeeSet gatherFeeSet = convertToGatherFees(entryFeeDataInfo.getGatherFees());
        OperateFeeSet operateFeeSet = convertToOperateFees(entryFeeDataInfo.getOperateFees());

        return EntryFeeInfo.builder()
                .entryFee(entryFeeDataInfo.getEntryFee())
                .gatherFeeSet(gatherFeeSet)
                .operateFeeSet(operateFeeSet)
                .etc(entryFeeDataInfo.getEtc())
                .build();
    }

    @Named("convertToEntryDataInfo")
    default EntryFeeDataInfo convertToEntryDataInfo(EntryFeeInfo entryFeeInfo) {

        return EntryFeeDataInfo.builder()
                .entryFee(entryFeeInfo.getEntryFee())
                .gatherFees(convertToGatherFeeStrings(entryFeeInfo.getGatherFeeSet().getGatherFees()))
                .operateFees(convertToOperateFeeStrings(entryFeeInfo.getOperateFeeSet().getOperateFees()))
                .etc(entryFeeInfo.getEtc())
                .build();
    }

    default List<String> convertToGatherFeeStrings(EnumSet<GatherFee> gatherFeeSet) {
        List<String> gatherFees = new ArrayList<>();
        gatherFeeSet.forEach(gatherFee -> gatherFees.add(gatherFee.name()));
        return gatherFees;
    }

    default List<String> convertToOperateFeeStrings(EnumSet<OperateFee> operateFeeSet) {
        List<String> operateFees = new ArrayList<>();
        operateFeeSet.forEach(operateFee -> operateFees.add(operateFee.name()));
        return operateFees;
    }

    default GatherFeeSet convertToGatherFees(List<String> requestGatherFees) {
        EnumSet<GatherFee> gatherFees = EnumSet.noneOf(GatherFee.class);
        requestGatherFees.forEach(g -> gatherFees.add(GatherFee.valueOf(g)));
        return new GatherFeeSet(gatherFees);
    }

    default OperateFeeSet convertToOperateFees(List<String> requestOperateFees) {
        EnumSet<OperateFee> operateFees = EnumSet.noneOf(OperateFee.class);
        requestOperateFees.forEach(o -> operateFees.add(OperateFee.valueOf(o)));
        return new OperateFeeSet(operateFees);
    }

    @Named("setConvertToList")
    default List<Long> setConvertToList(Set<Long> longSet) {
        return longSet.stream().toList();
    }
}
