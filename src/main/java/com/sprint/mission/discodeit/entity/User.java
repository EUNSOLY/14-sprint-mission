package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;

@Getter
public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private String password;


    public User(String username, String email, String password) {
        super();
        this.name = username;
        this.email = email;
        this.password = password;
    }

    public void update(String newUsername, String newEmail, String newPassword) {
        boolean anyValueUpdated = false;
        if (newUsername != null) {
            this.name = newUsername;
            anyValueUpdated = true;
        }
        if (newEmail != null) {
            this.email = newEmail;
            anyValueUpdated = true;
        }
        if (newPassword != null) {
            this.password = newPassword;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            super.updatedAt();
        }
    }

    @Override
    public String toString() {
        return String.format("User ( \n" +
                        " id=%s, createdAt=%s, updatedAt=%s \n" +
                        " name=%s, email=%s, password=%s \n" +
                        ")",
                super.getId(), super.getCreatedAt(), super.getUpdatedAt(),
                this.name, this.email, this.password // 비밀번호는 노출안되도록 제거 할 필요있음
        );
    }
}