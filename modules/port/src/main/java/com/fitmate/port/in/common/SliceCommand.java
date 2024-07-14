package com.fitmate.port.in.common;

import lombok.Getter;

@Getter
public class SliceCommand {
    private final Integer page;
    private final Integer size;
    private final String sortProperty;
    private final SortDir sortDir;

    public SliceCommand(Integer page, Integer size, SortDir sortDir, String sortProperty) {
        this.page = page;
        this.size = size;
        this.sortDir = sortDir;
        this.sortProperty = sortProperty;
    }

    public enum SortDir {
        ASC,
        DESC,
    }
}
