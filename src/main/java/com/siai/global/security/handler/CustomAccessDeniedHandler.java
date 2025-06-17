package com.siai.global.security.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Spring Security에서 권한이 부족하여 접근이 거부될때 호출되는 핸들러
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final HandlerExceptionResolver resolver;

	public CustomAccessDeniedHandler(
		@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
	) {
		this.resolver = resolver;
	}

	@Override
	public void handle(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final AccessDeniedException accessDeniedException
	) throws IOException, ServletException {
		resolver.resolveException(request, response, null, accessDeniedException);
	}
}
