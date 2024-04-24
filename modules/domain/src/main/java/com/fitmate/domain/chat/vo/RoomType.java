package com.fitmate.domain.chat.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomType {
    GROUP("그룹"),
    DM("개인");
    public final String label;
}
