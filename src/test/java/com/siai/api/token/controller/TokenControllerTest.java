package com.siai.api.token.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siai.api.mock.annotation.WithCustomMockUser;
import com.siai.api.token.service.TokenService;
import com.siai.domain.entity.Role;

@WithCustomMockUser(role = Role.MEMBER)
@WebMvcTest(TokenController.class)
class TokenControllerTest {

	@MockitoBean
	private TokenService tokenService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Authorization헤더가 포함된 경우는 AT재발급을 성공적으로 수행한다.")
	void createAccessToken() throws Exception{
		// given
		String token = "Bearer testAccessToken";

		// when & then
		mockMvc.perform(post("/api/auth/access-token/issue")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Authorization헤더가 포함되지 않았다면 AT재발급 실패")
	void createAccessToken_ThrowsException_IfAuthorizationIsNull() throws Exception{

		// when & then
		mockMvc.perform(post("/api/auth/access-token/issue")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
			.andExpect(status().isUnauthorized())
			.andDo(print());
	}
}