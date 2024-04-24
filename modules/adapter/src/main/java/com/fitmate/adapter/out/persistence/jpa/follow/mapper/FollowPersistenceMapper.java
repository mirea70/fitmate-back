package com.fitmate.adapter.out.persistence.jpa.follow.mapper;

import com.fitmate.adapter.out.persistence.jpa.follow.dto.FollowDetailJpaResponse;
import com.fitmate.port.out.follow.FollowDetailResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowPersistenceMapper {
    public FollowDetailResponse jpaResponseToResponse(FollowDetailJpaResponse jpaResponse) {
        return new FollowDetailResponse(jpaResponse.getAccountId(),
                jpaResponse.getProfileImageId(), jpaResponse.getNickName());
    }

    public List<FollowDetailResponse> jpaResponsesToResponses(List<FollowDetailJpaResponse> jpaResponses) {
        return jpaResponses.stream()
                .map(this::jpaResponseToResponse)
                .toList();
    }
}
