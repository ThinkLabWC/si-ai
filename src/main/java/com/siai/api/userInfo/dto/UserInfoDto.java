package com.siai.api.userInfo.dto;

import com.siai.domain.entity.Role;
import com.siai.domain.entity.User;

import lombok.Builder;

public class UserInfoDto {
	@Builder
	public record Response(
		String email,
		String name,
		Role role
	){
		public static Response of(User user){
			return new Response(
				user.getEmail(),
				user.getName(),
				user.getRole()
			);
		}
	}
}
