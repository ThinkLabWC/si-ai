package com.siai.global.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.siai.global.error.ErrorCode;
import com.siai.global.error.exception.AuthenticationException;
import com.siai.global.security.details.CustomUserDetailsService;
import com.siai.global.security.jwt.constant.TokenType;
import com.siai.global.security.util.JwtUtil;
import com.siai.global.util.AuthorizationHeaderUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final CustomUserDetailsService customUserDetailService;
	private final JwtUtil tokenManager;


	@Override
	protected void doFilterInternal(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final FilterChain filterChain
	) throws ServletException, IOException
	{
		// 1. Authorization Header 검증
		String authorizationHeader = request.getHeader("Authorization");
		if(authorizationHeader != null){
			AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

			// 2. 토큰 검증
			String token = authorizationHeader.split(" ")[1];
			if(tokenManager.validateToken(token)){

				// 3. 토큰 타입
				Claims tokenClaims = tokenManager.getTokenClaims(token);
				String tokenType = tokenClaims.getSubject();

				if (!TokenType.isAccessToken(tokenType)) {
					throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
				}

				// 4. Claim을 통해 호원 이메일 조회
				String email = tokenClaims.get("email", String.class);
				UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

				// 5. /admin으로 시작하는 경로라면 관리자만 접근하도록 설정
				if (request.getRequestURI().startsWith("/api/admin")) {
					if(!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						return;
					}
				}

				if (userDetails != null) {
					//UserDetails, Password, Role -> 접근 권한 인증 Token 생성
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

					//현재 Request의 Security Context에 접근 권한 설정
					SecurityContextHolder.getContext()
						.setAuthentication(usernamePasswordAuthenticationToken);
				}
			};

		}
		filterChain.doFilter(request, response); //다음 필터로 넘김
	}
}
