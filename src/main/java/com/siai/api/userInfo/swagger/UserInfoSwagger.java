package com.siai.api.userInfo.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.siai.api.userInfo.dto.UserInfoDto;
import com.siai.domain.common.Response;
import com.siai.domain.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "member", description = "회원가입/회원정보조회")
public interface UserInfoSwagger {

	@Tag(name = "member")
	@Operation(summary = "회원정보조회 API", description = "회원정보조회 API")
	ResponseEntity<Response<UserInfoDto.Response>> getUserInfo(@AuthenticationPrincipal User user);
}
