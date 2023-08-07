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
        var authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        var token = getAccessToken(authorizationHeader);

        if (jwtProvider.validToken(token)) {
            var authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.info("유효한 토큰이 존재하지 않습니다. {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        return (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) ? authorizationHeader.substring(TOKEN_PREFIX.length()) : null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var excludePath = List.of("/api/login", "/hello-world", "/h2-console", "/api/refresh-token");
        var path = request.getRequestURI();
        return excludePath.stream().anyMatch(path::startsWith);
    }
}
