package com.siai.domain.dto;

import com.siai.domain.entity.Role;
import com.siai.domain.entity.User;
import com.siai.global.security.jwt.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserInfoDto extends UserDto {
	private Long userId;
	private String email;
	private String password;
	private String name;
	private Role role;

	public static CustomUserInfoDto of(User user){
		return new CustomUserInfoDto(
			user.getId(),
			user.getEmail(),
			user.getPassword(),
			user.getName(),
			user.getRole());
	}
}
