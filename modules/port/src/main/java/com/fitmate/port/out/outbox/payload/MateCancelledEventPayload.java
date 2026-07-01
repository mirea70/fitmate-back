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
public class MateCancelledEventPayload implements EventPayload {
    private Long mateId;
    private Long writerId;
    private Long applierId;
    private String title;
    private String cancelReason;
    private boolean wasApproved;
}
