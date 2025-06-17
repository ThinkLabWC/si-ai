package com.siai.api.userInfo.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.siai.api.userInfo.dto.UserInfoDto;
import com.siai.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService {
	private final UserRepository userRepository;

	public UserInfoDto.Response getUserInfo(final String email) {
		final var user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
		return UserInfoDto.Response.of(user);
	}
}
