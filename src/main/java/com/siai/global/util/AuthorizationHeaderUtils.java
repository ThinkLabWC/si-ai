package com.siai.global.util;

import com.siai.global.error.ErrorCode;
import com.siai.global.error.exception.AuthenticationException;
import com.siai.global.security.jwt.constant.GrantType;

public class AuthorizationHeaderUtils {

	public static void validateAuthorization(String authorizationHeader) {

		// authorizationHeader Bearer 체크
		String[] authorizations = authorizationHeader.split(" ");
		if (authorizations.length < 2 || (!GrantType.BEARER.getType().equals(authorizations[0]))) {
			throw new AuthenticationException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
		}
	}

}
