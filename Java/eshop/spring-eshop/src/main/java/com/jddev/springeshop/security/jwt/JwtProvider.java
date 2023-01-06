package com.jddev.springeshop.security.jwt;

import com.jddev.springeshop.security.UserPrinciple;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface JwtProvider {
    String generateToken(UserPrinciple auth);

    Authentication getAuthentication(HttpServletRequest request);

    boolean isTokenValid(HttpServletRequest request);
}
