package com.siai.api.mock.security;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.siai.api.mock.annotation.WithCustomMockUser;
import com.siai.domain.entity.Role;
import com.siai.domain.entity.User;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

	@Override
	public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
		User user = User.builder()
			.name(annotation.name())
			.password(annotation.password())
			.build();

		UsernamePasswordAuthenticationToken token =
			new UsernamePasswordAuthenticationToken(user, user.getPassword(), buildGrantedAuthoritiesFromRole(annotation.role()));
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(token);
		return context;
	}

	private List<GrantedAuthority> buildGrantedAuthoritiesFromRole(final Role role) {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}
}
