package com.siai.api.userInfo.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.siai.api.userInfo.dto.UserInfoDto;
import com.siai.domain.entity.Role;
import com.siai.domain.entity.User;
import com.siai.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

	@InjectMocks
	private UserInfoService userInfoService;

	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("회원 정보를 조회한다.")
	void getUserInfo() {
		// given
		String email = "test@test.com";

		User user = User.builder()
			.id(1L)
			.name("test")
			.email("test@test.com")
			.role(Role.MEMBER)
			.build();

		given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

		// when
		UserInfoDto.Response response = userInfoService.getUserInfo(email);

		// then
		assertEquals(email, response.email());
	}

	@Test
	@DisplayName("이메일이 존재하지 않는 경우 - 회원 정보 조회 실패")
	void getUserInfo_ThrowsException_IfEmailExists() {
		// given
		String email = "test@test.com";

		// when
		given(userRepository.findByEmail(email)).willReturn(Optional.empty());

		// then
		assertThatThrownBy(
			() -> userInfoService.getUserInfo(email))
			.isInstanceOf(UsernameNotFoundException.class);
	}
}