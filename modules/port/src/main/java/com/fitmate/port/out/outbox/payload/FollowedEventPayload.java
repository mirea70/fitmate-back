package com.fitmate.port.out.outbox.payload;

import com.fitmate.port.out.outbox.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowedEventPayload implements EventPayload {
    private Long fromAccountId;
    private Long targetAccountId;
    private String fromNickName;
}
