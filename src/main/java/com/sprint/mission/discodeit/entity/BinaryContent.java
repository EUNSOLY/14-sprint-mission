package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity {
    private final String title;
    private final String imageUrl;


    public BinaryContent(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    @Override
    public void updatedAt() {
        throw new RuntimeException("업데이트가 불가능합니다.");
    }

}
