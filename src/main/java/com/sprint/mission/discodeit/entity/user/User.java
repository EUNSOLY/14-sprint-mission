package com.sprint.mission.discodeit.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @JoinColumn(name = "profile_id", columnDefinition = "uuid")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BinaryContent profile;

    @JsonBackReference // 객체 간 양방향 참조 시 발생하는 무한 재귀(순환 참조) 문제를 해결
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;

    public static User create(String username, String email, String password) {
        return new User(username, email, password);
    }


    private User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void update(String newUsername, String newEmail, String newPassword) {
        if (newUsername != null) {
            this.username = newUsername;
        }
        if (newEmail != null) {
            this.email = newEmail;
        }
        if (newPassword != null) {
            this.password = newPassword;
        }
    }

    public void updateProfile(BinaryContent profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return String.format("User ( \n" +
                        " id=%s, createdAt=%s, updatedAt=%s \n" +
                        " name=%s, email=%s, password=%s \n" +
                        ")",
                super.getId(), super.getCreatedAt(), super.getUpdatedAt(),
                this.username, this.email, this.password // 비밀번호는 노출안되도록 제거 할 필요있음
        );
    }
}