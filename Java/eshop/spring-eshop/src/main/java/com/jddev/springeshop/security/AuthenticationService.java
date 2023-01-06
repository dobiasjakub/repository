package com.jddev.springeshop.security;

import com.jddev.springeshop.persistance.entity.User;

public interface AuthenticationService {

    User signInAndReturnJWT(User signInRequest);

}
