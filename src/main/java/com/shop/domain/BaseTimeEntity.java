package com.shop.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
    1. 상속 관계 매핑이 아님
    2. @MappedSuperclass 가 선언되어 있는 클래스는 "엔티티"가 아니다. 당연히 테이블과 매핑 안됨
    3. 부모 클래스를 상속 받는 자식 클래스에게 매핑 정보만 제공하기 위함
    4. 조회, 검색이 불가능한 클래스
    5. 직접 생성할 일이 없기에 추상 클래스로 지정하는 것을 권장
    6. JPA에서 @Entity 클래스는 @Entity, @MappedSuperclass 로 지정한 클래스만 상속이 가능함
 */
@EntityListeners(value = {AuditingEntityListener.class}) // Auditing 기능 사용을 위해서 @EntityListeners 어노테이션 추가
@MappedSuperclass // 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
@Getter @Setter
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티가 생성되어 저장될 때 자동으로 시간 저장
    @Column(updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate // 엔티티의 값을 변경할 때 시간을 자동으로 저장
    private LocalDateTime updateDate;

}
