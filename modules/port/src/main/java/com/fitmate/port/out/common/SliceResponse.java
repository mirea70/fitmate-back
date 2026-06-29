package com.fitmate.port.out.common;

import java.util.List;

public record SliceResponse<T>(
        List<T> content,
        Integer currentPage,
        Integer size,
        Boolean first,
        Boolean last
) {
}
