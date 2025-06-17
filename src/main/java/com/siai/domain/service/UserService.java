package com.siai.domain.service;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siai.domain.dto.UserDto;
import com.siai.domain.entity.User;
import com.siai.domain.repository.UserRepository;
import com.siai.global.error.ErrorCode;
import com.siai.global.error.exception.AuthenticationException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	private final PasswordEncoder encoder;
	private final UserRepository userRepository;

	public UserDto.Response createUser(
		final UserDto.Request request
	){
		if(userRepository.existsByEmail(request.email())){
			throw new IllegalArgumentException("이미 존재하는 회원입니다.");
		};

		final var user = User.from(request);
		user.updatePassword(encoder.encode(user.getPassword()));
		final var savedUser = userRepository.save(user);

		return UserDto.Response.builder()
			.name(savedUser.getName())
			.email(savedUser.getEmail())
			.build();
	}

	@Transactional(readOnly = true)
	public User findByEmail(final String email){
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public User findUserByRefreshToken(final String refreshToken) {
		final var user = userRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
		LocalDateTime tokenExpirationTime = user.getTokenExpirationTime();
		if(tokenExpirationTime.isBefore(LocalDateTime.now())) {
			throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		}
		return user;
	}
}
