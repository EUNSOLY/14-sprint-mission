package com.sprint.mission.discodeit.controller.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {
    T content;
    Integer number;
    Integer size;
    Boolean hasNext;
    Long totalElements;

    public static <T> PageResponse<T> to(T content, int number, Integer size, Boolean hasNext, Long totalElements) {
        return new PageResponse<>(content, number, size, hasNext, totalElements);
    }
}
