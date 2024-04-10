package com.cardapi.card.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import java.io.IOException;

@Configuration
@Slf4j
public class SecurityConfig extends AbstractAuthenticationProcessingFilter {

    private final JwtTokenValidator tokenVerifier;

    public AccessTokenFilter(
            JwtTokenValidator jwtTokenValidator,
            AuthenticationManager authenticationManager) {

        super(AnyRequestMatcher.INSTANCE);
        setAuthenticationManager(authenticationManager);
        this.tokenVerifier = jwtTokenValidator;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {

        log.info("Attempting to authenticate for a request {}", request.getRequestURI());

        String authorizationHeader = extractAuthorizationHeaderAsString(request);
        AccessToken accessToken = null;
        try {
            accessToken = tokenVerifier.validateAuthorizationHeader(authorizationHeader);
        } catch (InvalidTokenException e) {
            throw new RuntimeException(e);
        }
        return this.getAuthenticationManager()
                .authenticate(new JwtAuthentication(accessToken));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        log.info("Successfully authentication for the request {}", request.getRequestURI());

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    private String extractAuthorizationHeaderAsString(HttpServletRequest request) {
        try {
            return request.getHeader("Authorization");
        } catch (Exception ex){
            throw new InvalidTokenException("There is no Authorization header in a request", ex);
        }
    }

}
