package com.siai.api.login.swagger;

import org.springframework.http.ResponseEntity;

import com.siai.api.login.dto.LoginDto.LoginDto;
import com.siai.domain.common.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "authentication", description = "로그인/로그아웃")
public interface LoginSwagger {

	@Tag(name = "authentication")
	@Operation(summary = "로그인 API", description = "로그인 API")
	ResponseEntity<Response<LoginDto.Response>> login(final LoginDto.Request request);
}
