package com.fitmate.port.in.common;

public interface SliceCommand {
    Integer page();
    Integer size();
    String sortProperty();
    SortDir sortDir();

    enum SortDir {
        ASC,
        DESC,
    }
}
