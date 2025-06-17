package com.siai.api.login.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.siai.api.login.dto.LoginDto.LoginDto;
import com.siai.domain.dto.CustomUserInfoDto;
import com.siai.domain.entity.Role;
import com.siai.domain.entity.User;
import com.siai.domain.service.UserService;
import com.siai.global.security.jwt.dto.JwtTokenDto;
import com.siai.global.security.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

	@InjectMocks
	private LoginService loginService;

	@Mock
	private UserService userService;

	@Mock
	private PasswordEncoder encoder;

	@Mock
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("로그인을 한다")
	void login() {
		// given
		User user = setUser();

		LoginDto.Request request = LoginDto.Request.builder()
			.email("test@test.com")
			.password("1234")
			.build();

		JwtTokenDto.Response token = JwtTokenDto.Response.builder()
									.grantType("Bearer")
									.accessToken("accessToken")
									.accessTokenExpireTime(new Date())
									.refreshToken("refreshToken")
									.refreshTokenExpireTime(new Date())
									.build();

		// when
		given(userService.findByEmail("test@test.com")).willReturn(user);
		given(encoder.matches(request.password(), user.getPassword())).willReturn(true);
		given(jwtUtil.createJwtTokenDto(any(CustomUserInfoDto.class))).willReturn(token);

		LoginDto.Response response = loginService.login(request);

		//then
		assertEquals(token.accessToken(), response.accessToken());
	}

	@Test
	@DisplayName("존재하지 않는 이메일로 요청할 경우 - 로그인 실패")
	public void login_ThrowsException_IfEmailNotExist() {
		//given
		LoginDto.Request request = LoginDto.Request.builder()
			.email("test@test.com")
			.password("1234")
			.build();
		
		given(userService.findByEmail(request.email()))
			.willThrow(new UsernameNotFoundException("회원이 존재하지 않습니다."));

		//when & then
		assertThatThrownBy(
			() -> loginService.login(request))
			.isInstanceOf(UsernameNotFoundException.class);
	}

	@Test
	@DisplayName("비밀번호가 일치하지 않을 경우 - 로그인 실패")
	public void login_ThrowsException_IfPasswordNotMatch() {
		//given
		LoginDto.Request request = LoginDto.Request.builder()
			.email("test@test.com")
			.password("1234")
			.build();

		User user = setUser();

		given(userService.findByEmail(request.email())).willReturn(user);
		given(encoder.matches(request.password(), user.getPassword())).willReturn(false);


		//when & then
		assertThatThrownBy(
			() -> loginService.login(request))
			.isInstanceOf(BadCredentialsException.class);
	}

	private User setUser(){
		return User.builder()
			.id(1L)
			.name("test")
			.email("test@test.com")
			.role(Role.MEMBER)
			.build();
	}
}