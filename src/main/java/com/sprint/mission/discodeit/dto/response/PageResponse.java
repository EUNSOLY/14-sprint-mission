package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content, Integer number,
        Integer size, Boolean hasNext,
        Long totalElements
) {
    public static <T> PageResponse<T> to(List<T> content, int number, Integer size, Boolean hasNext, Long totalElements) {
        return new PageResponse<>(content, number, size, hasNext, totalElements);
    }
}
