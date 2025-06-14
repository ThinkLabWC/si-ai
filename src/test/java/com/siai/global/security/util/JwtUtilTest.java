package com.siai.global.security.util;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.siai.domain.dto.CustomUserInfoDto;
import com.siai.domain.entity.Role;
import com.siai.domain.entity.User;
import com.siai.global.security.jwt.dto.JwtTokenDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 클래스 별로 테스트 인스턴스가 생성
class JwtUtilTest {

	private JwtUtil jwtUtil;
	private String secretKey;
	private String accessTokenExpirationTime;
	private String refreshTokenExpirationTime;
	private Key base64EncodedSecretKey;

	@BeforeAll
	public void init(){
		secretKey = "jun12312412412312312312412312312421412jun12312412412312312312412312312421412jun12312412412312312312412312312421412";
		accessTokenExpirationTime = "900000";
		refreshTokenExpirationTime = "1209600000";
		jwtUtil = new JwtUtil(secretKey, accessTokenExpirationTime, refreshTokenExpirationTime);
		base64EncodedSecretKey = jwtUtil.getKeyFromBase64EncodedKey(secretKey);
	}

	@Test
	@DisplayName("CustomUser를 기반으로 token을 생성한다.")
	void createJwtTokenDto() {
		User user = setUser();
		CustomUserInfoDto infoDto = CustomUserInfoDto.of(user);
		JwtTokenDto.Response token = jwtUtil.createJwtTokenDto(infoDto);
		assertThat(token, notNullValue());
		System.out.println("token = " + token);
	}

	@Test
	@DisplayName("CustomUser를 기반으로 AT토큰을 생성한다.")
	void createAccessToken() {
		User user = setUser();
		CustomUserInfoDto infoDto = CustomUserInfoDto.of(user);

		String accessToken = jwtUtil.createAccessToken(infoDto, new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime)));
		assertThat(accessToken, notNullValue());
	}

	@Test
	@DisplayName("CustomUser를 기반으로 RT토큰을 생성한다.")
	void createRefreshToken() {
		User user = setUser();
		CustomUserInfoDto infoDto = CustomUserInfoDto.of(user);

		String refreshToken = jwtUtil.createRefreshToken(infoDto, new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime)));
		assertThat(refreshToken, notNullValue());
	}

	@Test
	@DisplayName("주어진 토큰을 기반으로 Claim정보를 추출한다.")
	void getTokenClaims() {
		User user = setUser();
		CustomUserInfoDto infoDto = CustomUserInfoDto.of(user);
		JwtTokenDto.Response token = jwtUtil.createJwtTokenDto(infoDto);
		Claims claims = jwtUtil.getTokenClaims(token.accessToken());
		System.out.println("claims = " + claims);
		assertThat(claims, notNullValue());
	}

	@Test
	@DisplayName("Plain Text인 Secret Key가 Base64 형식으로 디코딩이 정상적으로 수행한다.")
	void getKeyFromBase64EncodedKey() {
		byte[] keyBytes = Decoders.BASE64.decode(String.valueOf(secretKey));
		assertEquals(base64EncodedSecretKey, Keys.hmacShaKeyFor(keyBytes));
	}

	private User setUser(){
		return User.builder()
			.id(1L)
			.name("test")
			.email("test@test.com")
			.role(Role.MEMBER)
			.build();
	}
}