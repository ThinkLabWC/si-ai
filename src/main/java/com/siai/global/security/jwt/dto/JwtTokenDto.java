package com.siai.global.security.jwt.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

public class JwtTokenDto{
	@Builder
	public record Response(
		String grantType,
		String accessToken,
		// @JsonFormat - JSON 출력에 대한 필드 및/또는 속성의 형식을 지정하는 방법
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
		Date accessTokenExpireTime,
		String refreshToken,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
		Date refreshTokenExpireTime
	){

	}
}
