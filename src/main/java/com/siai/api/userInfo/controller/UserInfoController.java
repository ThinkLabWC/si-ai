package com.siai.api.userInfo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siai.api.userInfo.dto.UserInfoDto;
import com.siai.api.userInfo.service.UserInfoService;
import com.siai.api.userInfo.swagger.UserInfoSwagger;
import com.siai.domain.common.Response;
import com.siai.domain.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserInfoController implements UserInfoSwagger {
	private final UserInfoService userInfoService;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/me")
	public ResponseEntity<Response<UserInfoDto.Response>> getUserInfo(
		@AuthenticationPrincipal User user
	){
		final var userInfo = userInfoService.getUserInfo(user.getEmail());
		return ResponseEntity.ok(Response.ok(userInfo));
	}
}
