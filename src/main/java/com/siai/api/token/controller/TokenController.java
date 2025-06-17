package com.siai.api.token.controller;

import static com.siai.global.error.ErrorCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siai.api.token.dto.AccessTokenDto;
import com.siai.api.token.service.TokenService;
import com.siai.api.token.swagger.TokenSwagger;
import com.siai.domain.common.Response;
import com.siai.global.error.ExceptionDto;
import com.siai.global.util.AuthorizationHeaderUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TokenController implements TokenSwagger {

	private final TokenService tokenService;

	@PostMapping("/access-token/issue")
	public ResponseEntity<Response<AccessTokenDto.Response>> createAccessToken(
		final HttpServletRequest httpServletRequest
	) {
		final String authorizationHeader = httpServletRequest.getHeader("Authorization");
		if(authorizationHeader != null){
			AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

			String refreshToken = authorizationHeader.split(" ")[1];
			AccessTokenDto.Response accessTokenResponseDto = tokenService.createAccessTokenByRefreshToken(refreshToken);
			return ResponseEntity.ok(Response.ok(accessTokenResponseDto));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(Response.fail(new ExceptionDto(NOT_EXISTS_AUTHORIZATION, "Authorization header is missing")));

	}
}
