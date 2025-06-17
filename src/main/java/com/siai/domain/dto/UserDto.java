package com.siai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class UserDto {
	@Builder
	public record Request(
		@Schema(description = "email", example = "test@test.com")
		@NotBlank(message = "이메일은 필수 입력값입니다.") //null, "", " " 모두 허용X
		@Email(message = "이메일 형식에 맞지 않습니다.")
		String email,
		@Schema(description = "password", example = "1234")
		@NotBlank(message = "비밀번호는 필수 입력값입니다.")
		String password,
		@Schema(description = "name", example = "test")
		@NotBlank(message = "이름은 필수 입력값입니다.")
		String name
	){

	}

	@Builder
	public record Response(
		String email,
		String name
	){
	}
}
