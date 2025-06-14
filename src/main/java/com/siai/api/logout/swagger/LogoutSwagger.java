package com.siai.api.logout.swagger;

import org.springframework.http.ResponseEntity;

import com.siai.domain.common.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "authentication", description = "로그인/로그아웃")
public interface LogoutSwagger {

	@Tag(name = "authentication")
	@Operation(summary = "로그아웃 API", description = "로그아웃시 refresh token 만료 처리")
	ResponseEntity<Response<String>> logout(final HttpServletRequest httpServletRequest);
}
