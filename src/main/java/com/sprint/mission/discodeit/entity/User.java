package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
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
    
    public void update(String name, String phone, UserStatus status, String nickname) {
        if (name != null) this.name = name;
        if (phone != null) this.phone = phone;
        if (status != null) this.status = status;
        if (nickname != null) this.nickname = nickname;
        super.changeUpdatedAt();
    }

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