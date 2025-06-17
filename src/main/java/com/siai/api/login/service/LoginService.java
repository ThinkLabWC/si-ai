package com.siai.api.login.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.siai.api.login.dto.LoginDto.LoginDto;
import com.siai.domain.dto.CustomUserInfoDto;
import com.siai.domain.service.UserService;
import com.siai.global.security.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder encoder;

	public LoginDto.Response login(
		final LoginDto.Request request
	){
		final var user = userService.findByEmail(request.email());

		//암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
		if (!encoder.matches(request.password(), user.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		final var customUserInfoDto = CustomUserInfoDto.of(user);
		final var token = jwtUtil.createJwtTokenDto(customUserInfoDto);
		user.updateRefreshToken(token);
		return LoginDto.Response.of(token);
	}
}
