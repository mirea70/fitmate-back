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
public class MateReminderEventPayload implements EventPayload {
    private Long mateId;
    private Long accountId;
    private String title;
}
