package com.rk.blogging.exceptions;

import io.jsonwebtoken.ExpiredJwtException;


public class TokenExceptions  extends RuntimeException {
    public TokenExceptions(String message) {
        super(message);
    }
}