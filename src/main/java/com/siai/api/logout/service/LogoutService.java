package com.siai.api.logout.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siai.domain.entity.User;
import com.siai.domain.service.UserService;
import com.siai.global.error.ErrorCode;
import com.siai.global.error.exception.AuthenticationException;
import com.siai.global.security.jwt.constant.TokenType;
import com.siai.global.security.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {
	private final UserService userService;
	private final JwtUtil jwtUtil;

	public void logout(final String accessToken){

		// 1. 토큰 검증
		jwtUtil.validateToken(accessToken);

		// 2. 토큰 타입 확인
		final Claims tokenClaims = jwtUtil.getTokenClaims(accessToken);
		final String tokenType = tokenClaims.getSubject();
		if(!TokenType.isAccessToken(tokenType)){
			throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
		}

		// 3. refresh token 만료 처리
		final String email = String.valueOf(tokenClaims.get("email"));
		final User user = userService.findByEmail(email);
		user.expireRefreshToken(LocalDateTime.now());
	}
}
