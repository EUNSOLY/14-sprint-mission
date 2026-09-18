package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @MappedSuperclass :  DB에 테이블을 만들지않고 해당 클래스를 상속받으면 필드를 그대로 엔티티에 포함시킨다.
 * @EntityListeners(AuditingEntityListener.class) - 클래스의 필드를 자동으로 감시, 엔티티 생성/수정 시점에 특정 동작을 수행
 * - AuditingEntityListener는 엔티티가 저장되거나 업데이트 될 때 호출됨
 * - 그때 @CreatedDate, @LastModifiedDate가 붙은 필드를 찾아 자동으로 값을 넣어줌 이게 바로 감사(Auditing 기능)ㄴ
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(updatable = false, nullable = false) // 업데이트 불가로 선언
    private Instant createdAt;
}
