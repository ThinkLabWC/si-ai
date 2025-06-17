package com.siai.api.userInfo.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.siai.api.mock.annotation.WithCustomMockUser;
import com.siai.api.userInfo.dto.UserInfoDto;
import com.siai.api.userInfo.service.UserInfoService;
import com.siai.domain.entity.Role;
import com.siai.global.security.details.CustomUserDetailsService;

@WithCustomMockUser(role = Role.ADMIN)
@WebMvcTest(UserInfoController.class)
class UserInfoControllerTest {

	@Autowired
	private WebApplicationContext context; //setUp()에서 사용하기 위해 주입

	@MockitoBean
	private UserInfoService userInfoService;

	@MockitoBean
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders
			.webAppContextSetup(context)
			.apply(springSecurity())
			.build();
	}

	@Test
	@DisplayName("관리자는 자신의 정보를 반환한다.")
	void getUserInfo() throws Exception{
		// given
		UserInfoDto.Response response = UserInfoDto.Response.builder()
			.name("test")
			.email("test@test.com")
			.role(Role.ADMIN)
			.build();

		given(userInfoService.getUserInfo(response.email())).willReturn(response);

		// when then
		mockMvc.perform(get("/api/admin/me")
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf()))
					.andExpect(status().isOk())
					.andDo(print())
					.andReturn();
	}
}