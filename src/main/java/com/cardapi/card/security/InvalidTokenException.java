package com.cardapi.card.security;

import javax.naming.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String message) {
        super(message);
    }

//    public InvalidTokenException(String message, Throwable cause) {
//        super(message,cause);
//    }
}
