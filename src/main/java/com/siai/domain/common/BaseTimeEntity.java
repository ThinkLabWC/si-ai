package com.siai.domain.common;

import java.time.LocalDateTime;

import org.hibernate.grammars.hql.HqlParser;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass // baseEntity를 상속한 엔터티들은 아래 필드들을 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class) //Auditing 기능을 사용하겠다, 자동으로 값을 매핑시키겠다는 의미
public abstract class BaseTimeEntity {
	@CreatedDate
	private LocalDateTime createdDate;
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
}
