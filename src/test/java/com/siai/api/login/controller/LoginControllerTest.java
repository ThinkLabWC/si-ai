package com.siai.api.login.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siai.api.login.dto.LoginDto.LoginDto;
import com.siai.api.login.service.LoginService;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

	@MockitoBean
	private LoginService loginService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("올바른 요청으로 로그인을 한다.")
	void login() throws Exception{
		// given
		LoginDto.Request request = LoginDto.Request.builder()
			.email("test@test.com")
			.password("1234")
			.build();

		// when then
		mockMvc.perform(
				post("/api/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
	}

	@Test
	@DisplayName("비밀번호가 null일때 로그인 요청 실패")
	void login_ThrowsException_IfPasswordIsNull() throws Exception{
		// given
		LoginDto.Request request = LoginDto.Request.builder()
			.email("test@test.com")
			.password(null)
			.build();

		// when then
		mockMvc.perform(
				post("/api/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf()))
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andReturn();
	}

	@Test
	@DisplayName("이메일 형식이 아닐때 로그인 요청 실패")
	void login_ThrowsException_IfEmailIsInvalid() throws Exception{
		// given
		LoginDto.Request request = LoginDto.Request.builder()
			.email("test")
			.password("1234")
			.build();

		// when then
		mockMvc.perform(
				post("/api/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf()))
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andReturn();
	}
}