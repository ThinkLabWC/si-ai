package com.siai.global.security.details;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siai.domain.entity.User;
import com.siai.domain.repository.UserRepository;
import com.siai.domain.dto.CustomUserInfoDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final ModelMapper mapper;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
			.orElseThrow(()-> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. :" + email));

		CustomUserInfoDto dto = CustomUserInfoDto.of(user);

		return new CustomUserDetails(dto);
	}
}
