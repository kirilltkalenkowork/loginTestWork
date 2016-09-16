package org.tkalenko.spring.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class NoOpAuthenticationManager implements AuthenticationManager {

    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        return authentication;
    }
}
