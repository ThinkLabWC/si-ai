package com.siai.api.signup.controller;

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
import com.siai.domain.dto.UserDto;
import com.siai.domain.service.UserService;

@WebMvcTest(SignUpController.class)
@AutoConfigureMockMvc(addFilters = false)
class SignUpControllerTest {

	// @MockitoBean
	// private SignUpController signUpController;

	@MockitoBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("올바른 요청 방식으로 회원가입을 한다.")
	void signup() throws Exception{
		// given
		UserDto.Request request = UserDto.Request.builder()
			.email("test@test.com")
			.password("1234")
			.name("천규")
			.build();

		// when then
		mockMvc.perform(
			post("/api/auth/signup")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
	}

	@Test
	@DisplayName("비밀번호가 null일때 회원가입 요청 실패")
	void signup_ThrowsException_IfPasswordIsNull() throws Exception{
		// given
		UserDto.Request request = UserDto.Request.builder()
			.email("test")
			.password(null)
			.name("천규")
			.build();

		// when then
		mockMvc.perform(
				post("/api/auth/signup")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf()))
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andReturn();
	}

	@Test
	@DisplayName("이름이 null일때 회원가입 요청 실패")
	void signup_ThrowsException_IfNameIsNull() throws Exception{
		// given
		UserDto.Request request = UserDto.Request.builder()
			.email("test")
			.password("1234")
			.name(null)
			.build();

		// when then
		mockMvc.perform(
				post("/api/auth/signup")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf()))
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andReturn();
	}

	@Test
	@DisplayName("이메일 형식이 아닐때 회원가입 요청 실패")
	void signup_ThrowsException_IfEmailIsInvalid() throws Exception{
		// given
		UserDto.Request request = UserDto.Request.builder()
			.email("test")
			.password("1234")
			.name("천규")
			.build();

		// when then
		mockMvc.perform(
				post("/api/auth/signup")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf()))
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andReturn();
	}
}