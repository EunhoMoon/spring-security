package me.janek.securityjava.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.janek.securityjava.common.JwtProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public final static String HEADER_AUTHORIZATION = "Authorization";

    public final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Jwt Filter 호출");
        var authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        var token = getAccessToken(authorizationHeader);

        var validationStatus = jwtProvider.validToken(token);

        switch (validationStatus) {
            case INVALID_TOKEN -> throw new IllegalArgumentException(validationStatus.getDescribe());
            default -> log.info("필터 통과");
        }

        var authentication = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        return (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) ? authorizationHeader.substring(TOKEN_PREFIX.length()) : null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var excludePath = List.of("/api/login", "/hello-world", "/h2-console");
        var path = request.getRequestURI();
        return excludePath.stream().anyMatch(path::startsWith);
    }
}
