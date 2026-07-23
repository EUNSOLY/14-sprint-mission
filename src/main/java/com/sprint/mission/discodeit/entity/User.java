package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
    private String name; // 이름
    private String phone;
    private String nickname;
    private UserStatus status;


    public User(String name, String phone, String nickname, UserStatus status) {
        super();
        this.name = name;
        this.phone = phone;
        this.nickname = nickname;
        this.status = status;
    }

    public void changeName(String name) {
        this.name = name;
        super.changeUpdatedAt();
    }

    public void changePhone(String phone) {
        this.phone = phone;
        super.changeUpdatedAt();
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
        super.changeUpdatedAt();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
        super.changeUpdatedAt();
    }

    ;

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public String getNickname() {
        return this.nickname;
    }

    @Override
    public String toString() {
        return String.format("User ( \n" +
                        " id=%s, createdAt=%s, updateAt=%s \n" +
                        " name=%s, nickname=%s, phone=%s, status=%s \n" +
                        ")",
                super.getId(), super.getCreatedAt(), super.getUpdatedAt(),
                this.name, this.nickname, this.phone, this.status
        );
    }
}