package com.siai.api.login.dto.LoginDto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.siai.global.security.jwt.dto.JwtTokenDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class LoginDto {

	@Builder
	public record Request(
		@Schema(description = "email", example = "test@test.com")
		@NotBlank(message = "이메일은 필수 입력값입니다.") //null, "", " " 모두 허용X
		@Email(message = "이메일 형식에 맞지 않습니다.")
		String email,
		@Schema(description = "password", example = "1234")
		@NotBlank(message = "비밀번호는 필수 입력값입니다.")
		String password
	) {
	}

	@Builder
	public record Response(
		@Schema(description = "grantType", example = "Bearer")
		String grantType,

		@Schema(description = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2NTg0ODAyOTYsImV4cCI6MTY1ODQ4MTE5NiwibWVtYmVySWQiOjEsInJvbGUiOiJBRE1JTiJ9.qr5fOs9NIO5UYJzqgisESOXorASLphj04uyjF1Breolj4cou_k6py0egF8f3OxWjQXps3on7Ko3jwIaL_2voRg", required = true)
		String accessToken,

		@Schema(description = "access token 만료 시간", example = "2022-07-22 18:13:16")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
		Date accessTokenExpireTime,

		@Schema(description = "refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0aASDgwMjk3LCJleHAiOjE2NTk2ODk4OTYsIm1lbWJlcklkIjoxfQ.hxgq_DIU554lUnUCSAGTYOiaXLXwgpyIM2h8a5de3ALEOY-IokElS6VbMmVTKlpRaLfEzzcr3FkUDrNisRt-bA", required = true)
		String refreshToken,

		@Schema(description = "refresh token 만료 시간", example = "2022-08-05 18:13:16", required = true)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
		Date refreshTokenExpireTime
	) {
		public static Response of(JwtTokenDto.Response jwtTokenDto) {
			return Response.builder()
				.grantType(jwtTokenDto.grantType())
				.accessToken(jwtTokenDto.accessToken())
				.accessTokenExpireTime(jwtTokenDto.accessTokenExpireTime())
				.refreshToken(jwtTokenDto.refreshToken())
				.refreshTokenExpireTime(jwtTokenDto.refreshTokenExpireTime())
				.build();
		}
	}
}
