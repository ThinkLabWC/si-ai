package com.siai.global.error.exception;

import com.siai.global.error.ErrorCode;

public class AuthenticationException extends BusinessException {

	public AuthenticationException(ErrorCode errorCode) {
		super(errorCode);
	}

}

