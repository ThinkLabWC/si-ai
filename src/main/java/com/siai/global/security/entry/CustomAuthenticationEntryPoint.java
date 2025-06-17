package com.siai.global.security.entry;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

// Spring Security에서 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출되는 핸들러
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final HandlerExceptionResolver resolver;

	public CustomAuthenticationEntryPoint(
		@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
	) {
		this.resolver = resolver;
	}

	@Override
	public void commence(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull AuthenticationException authException
	) {

		resolver.resolveException(request, response, null, authException);
	}
}


