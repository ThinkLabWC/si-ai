package com.siai.global.security.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.siai.global.error.ErrorCode;
import com.siai.global.error.exception.AuthenticationException;
import com.siai.global.security.jwt.constant.GrantType;
import com.siai.global.security.jwt.constant.TokenType;
import com.siai.domain.dto.CustomUserInfoDto;
import com.siai.global.security.jwt.dto.JwtTokenDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	private final Key key;
	private final String accessTokenExpirationTime;
	private final String refreshTokenExpirationTime;

	public JwtUtil(
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.access-token-expiration-time}") String accessTokenExpirationTime,
		@Value("${jwt.refresh-token-expiration-time}") String refreshTokenExpirationTime
	) {
		this.key = getKeyFromBase64EncodedKey(secretKey);
		this.accessTokenExpirationTime = accessTokenExpirationTime;
		this.refreshTokenExpirationTime = refreshTokenExpirationTime;
	}

	public JwtTokenDto.Response createJwtTokenDto(CustomUserInfoDto infoDto) {
		Date accessTokenExpireTime = createAccessTokenExpireTime();
		Date refreshTokenExpireTime = createRefreshTokenExpireTime();

		String accessToken = createAccessToken(infoDto, accessTokenExpireTime);
		String refreshToken = createRefreshToken(infoDto, refreshTokenExpireTime);

		return JwtTokenDto.Response.builder()
			.grantType(GrantType.BEARER.getType())
			.accessToken(accessToken)
			.accessTokenExpireTime(accessTokenExpireTime)
			.refreshToken(refreshToken)
			.refreshTokenExpireTime(refreshTokenExpireTime)
			.build();
	}

	public Date createAccessTokenExpireTime() {
		return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
	}

	public Date createRefreshTokenExpireTime() {
		return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
	}

	public String createAccessToken(CustomUserInfoDto user, Date expirationTime) {
		// Claim객체를 생성하고 사용자 정보를 클레임에 추가
		Claims claims = Jwts.claims();
		claims.put("email", user.getEmail());
		claims.put("role", user.getRole());

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(TokenType.ACCESS.name()) // 토큰 제목
			.setIssuedAt(new Date()) // 토큰 발급 시간
			.setExpiration(expirationTime) // 토큰 만료 시간
			.signWith(key, SignatureAlgorithm.HS256)
			.setHeaderParam("typ", "JWT")
			.compact();
	}

	public String createRefreshToken(CustomUserInfoDto user, Date expirationTime) {
		// Claim객체를 생성하고 사용자 정보를 클레임에 추가
		Claims claims = Jwts.claims();
		claims.put("email", user.getEmail());
		claims.put("role", user.getRole());

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(TokenType.REFRESH.name())   // 토큰 제목
			.setIssuedAt(new Date())                // 토큰 발급 시간
			.setExpiration(expirationTime)          // 토큰 만료 시간
			.signWith(key, SignatureAlgorithm.HS256)
			.setHeaderParam("typ", "JWT")
			.compact();
	}

	// 토큰 유효성 검증

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.info("token 만료", e);
			throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED);
		} catch (Exception e) {
			log.info("유효하지 않은 token", e);
			throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
		}
	}

	// 주어진 토큰에서 Claim 추출

	public Claims getTokenClaims(String token) {
		Claims claims;
		try {
			claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			log.info("유효하지 않은 token", e);
			throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
		}
		return claims;
	}

	// SecretKey를 Base64로 디코딩하여 HMAC SHA 알고리즘을 사용한 키로 변환

	public Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
