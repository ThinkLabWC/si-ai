package com.siai.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.siai.global.security.details.CustomUserDetailsService;
import com.siai.global.security.entry.CustomAuthenticationEntryPoint;
import com.siai.global.security.filter.JwtAuthFilter;
import com.siai.global.security.handler.CustomAccessDeniedHandler;
import com.siai.global.security.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static final String[] AUTH_WHITELIST = {
		"/api/auth/signup",
		"/api/auth/login",
		"/api/auth/access-token/issue",
		"/swagger-ui/**",
		"/api-docs/**",
		"/swagger-resources/**"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//CSRF, CORS
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors((Customizer.withDefaults()));

		//세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 x
		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
			SessionCreationPolicy.STATELESS));

		//FormLogin, BasicHttp 비활성화
		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);

		//JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
		http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil),
			UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling((exceptionHandling) -> exceptionHandling
			.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler));

		//권한 규칙 작성
		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers(AUTH_WHITELIST).permitAll()
			.requestMatchers("/api/users/admin").hasRole("ADMIN")
			//@PreAuthorization 사용 -> 모든 경로에 대한 인증처리는 Pass
			.anyRequest().authenticated()
		);

		return http.build();
	}
}
