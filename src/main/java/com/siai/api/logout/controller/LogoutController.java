package com.siai.api.logout.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siai.api.logout.service.LogoutService;
import com.siai.api.logout.swagger.LogoutSwagger;
import com.siai.domain.common.Response;
import com.siai.global.util.AuthorizationHeaderUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LogoutController implements LogoutSwagger {
	private final LogoutService logoutService;

	@PostMapping("/logout")
	public ResponseEntity<Response<String>> logout(
		final HttpServletRequest httpServletRequest
	){
		final String authorizationHeader = httpServletRequest.getHeader("Authorization");

		if(authorizationHeader != null){
			AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

			final String accessToken = authorizationHeader.split(" ")[1];
			logoutService.logout(accessToken);
			return ResponseEntity.ok(Response.ok("logout success"));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
			body(Response.authError("Authorization header is missing"));
	}
}
