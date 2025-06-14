package com.siai.domain.entity;

import java.time.LocalDateTime;

import com.siai.domain.common.BaseTimeEntity;
import com.siai.domain.dto.UserDto;
import com.siai.global.security.jwt.dto.JwtTokenDto;
import com.siai.global.util.DateTimeUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {
	@Id @Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	@Column(length = 200)
	private String password;

	@Column(nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private Role role;

	@Column(length = 250)
	private String refreshToken;

	private LocalDateTime tokenExpirationTime;


	public static User from(UserDto.Request request){
		return new User(
			null,
			request.email(),
			request.password(),
			request.name(),
			Role.MEMBER,
			null,
			null
		);
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateRefreshToken(JwtTokenDto.Response token) {
		this.refreshToken = token.refreshToken();
		this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(token.refreshTokenExpireTime());
	}

	public void expireRefreshToken(LocalDateTime now) {
		this.tokenExpirationTime = now;
	}
}
