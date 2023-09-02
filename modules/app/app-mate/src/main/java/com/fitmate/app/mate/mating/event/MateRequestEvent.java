package com.fitmate.app.mate.mating.event;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;


@Getter
@RequiredArgsConstructor
public class MateRequestEvent  {

    private final MateEventDto.Request eventDto;

//    public MateRequestEvent(Object source, MateEventDto.Request eventDto) {
//        super(source);
//        this.eventDto = eventDto;
//    }
}
