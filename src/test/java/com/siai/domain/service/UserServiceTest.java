package com.siai.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.siai.domain.dto.UserDto;
import com.siai.domain.entity.Role;
import com.siai.domain.entity.User;
import com.siai.domain.repository.UserRepository;
import com.siai.global.error.exception.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder encoder;

	@Test
	@DisplayName("회원을 등록한다.")
	void createUser() {
		// given
		final UserDto.Request request = UserDto.Request.builder()
			.email("test@test.com")
			.name("test")
			.password("1234")
			.build();

		User user = User.from(request);

		// when
		given(userRepository.existsByEmail("test@test.com")).willReturn(false);
		given(userRepository.save(any(User.class))).willReturn(user);

		final UserDto.Response response = userService.createUser(request);
		verify(userRepository).save(any(User.class));

		// then
		assertEquals(request.email(), response.email());
	}

	@Test
	@DisplayName("이메일이 이미 존재하는 경우 - 회원 등록 실패")
	void createUser_ThrowsException_IfEmailExists() {
		// given
		final UserDto.Request request = UserDto.Request.builder()
			.email("test@test.com")
			.name("test")
			.password("1234")
			.build();

		// when
		given(userRepository.existsByEmail("test@test.com")).willReturn(true);

		// then
		assertThatThrownBy(
			() -> userService.createUser(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("RT토큰이 유효한 경우 회원을 조회한다.")
	void findUserByRefreshToken(){
		// given
		String refreshToken = "testRefreshToken";
		User user = setUser(refreshToken, LocalDateTime.now().plusDays(1));

		// when
		given(userRepository.findByRefreshToken(user.getRefreshToken())).willReturn(Optional.of(user));
		User result = userService.findUserByRefreshToken(user.getRefreshToken());

		// then
		assertEquals(user.getEmail(), result.getEmail());
	}

	@Test
	@DisplayName("RT토큰을 기반으로 회원을 찾지 못하면 예외를 던진다.")
	void findUserByRefreshToken_ThrowsException_IfUserNotExists(){
		// given
		String refreshToken = "invalidToken";
		User user = setUser(refreshToken, LocalDateTime.now().plusDays(1));

		given(userRepository.findByRefreshToken(user.getRefreshToken())).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(
			() -> userService.findUserByRefreshToken(refreshToken))
			.isInstanceOf(AuthenticationException.class);
	}

	@Test
	@DisplayName("RT토큰이 만료되었으면 예외를 던진다.")
	void findUserByRefreshToken_ThrowsException_IfTokenExpired() {
		// given
		String refreshToken = "expiredToken";
		User user = setUser(refreshToken, LocalDateTime.now().minusDays(1)); // 만료됨

		given(userRepository.findByRefreshToken(refreshToken)).willReturn(Optional.of(user));

		// when & then
		assertThatThrownBy(
			() -> userService.findUserByRefreshToken(refreshToken))
			.isInstanceOf(AuthenticationException.class);
	}

	private User setUser(String refreshToken, LocalDateTime tokenExpirationTime){
		return User.builder()
			.id(1L)
			.name("test")
			.email("test@test.com")
			.role(Role.MEMBER)
			.refreshToken(refreshToken)
			.tokenExpirationTime(tokenExpirationTime)
			.build();
	}
}