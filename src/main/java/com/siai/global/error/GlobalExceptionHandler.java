package com.siai.global.error;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.siai.domain.common.Response;
import com.siai.global.error.exception.AuthenticationException;

// @Hidden
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response<Map<String, String>>> handleValidationException(
		final MethodArgumentNotValidException ex
	) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		final ExceptionDto response = new ExceptionDto(ErrorCode.NOT_VALID_ERROR, errors.toString());
		return ResponseEntity.badRequest().body(Response.fail(response));
	}

	@ExceptionHandler
	public Response<ExceptionDto> handleAuthenticationException(
		final AuthenticationException e
	) {
		final ExceptionDto response = new ExceptionDto(ErrorCode.NOT_VALID_TOKEN, ErrorCode.NOT_VALID_TOKEN.getMessage());
		return Response.fail(response);
	}

	@ExceptionHandler
	public Response<ExceptionDto> handleAccessDeniedException(
		final AccessDeniedException e
	) {
		final ExceptionDto response = new ExceptionDto(ErrorCode.ACCESS_DENIED, ErrorCode.ACCESS_DENIED.getMessage());
		return Response.fail(response);
	}
}
