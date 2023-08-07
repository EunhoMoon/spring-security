package me.janek.securityjava.interfaces;

import lombok.RequiredArgsConstructor;
import me.janek.securityjava.domain.User;
import me.janek.securityjava.domain.UserAuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static me.janek.securityjava.config.JwtAuthenticationFilter.HEADER_AUTHORIZATION;
import static me.janek.securityjava.config.JwtAuthenticationFilter.TOKEN_PREFIX;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthenticationService userAuthenticationService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/api/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        var authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        var loginUser = userAuthenticationService.loadUserByUsername(request.getUsername());
        var tokenResponse = userAuthenticationService.createToken(loginUser);

        var httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTHORIZATION, TOKEN_PREFIX + tokenResponse.accessToken());

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @PostMapping("/api/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody TokenRefreshRequest request) {
        var newAccessToken = userAuthenticationService.refreshAccessToken(request);

        var httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTHORIZATION, TOKEN_PREFIX + newAccessToken);

        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }

    @GetMapping("/api/auth-test")
    public ResponseEntity<String> authTest(@AuthenticationPrincipal User user) {
        String result = "인증된 사용자: " + user.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
