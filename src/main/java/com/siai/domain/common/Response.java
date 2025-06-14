package com.siai.domain.common;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.siai.global.error.ExceptionDto;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Response<T>(
	@JsonIgnore
	HttpStatus httpStatus,
	boolean success,
	@Nullable T data,
	@Nullable ExceptionDto error
) {
	public static <T> Response<T> ok(@Nullable final T data) {
		return new Response<>(HttpStatus.OK, true, data, null);
	}

	public static <T> Response<T> created(@Nullable final T data) {
		return new Response<>(HttpStatus.CREATED, true, data, null);
	}

	public static <T> Response<T> fail(final ExceptionDto e) {
		return new Response<>(e.code().getHttpStatus(), false,null, e);
	}

	public static <T> Response<T> authError(@Nullable final T data){
		return new Response<>(HttpStatus.UNAUTHORIZED, false, data, null);
	}
}
