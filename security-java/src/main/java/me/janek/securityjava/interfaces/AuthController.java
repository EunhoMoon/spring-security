package me.janek.securityjava.interfaces;

import lombok.RequiredArgsConstructor;
import me.janek.securityjava.common.JwtProvider;
import me.janek.securityjava.domain.User;
import me.janek.securityjava.domain.UserAuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import static me.janek.securityjava.config.JwtAuthenticationFilter.HEADER_AUTHORIZATION;
import static me.janek.securityjava.config.JwtAuthenticationFilter.TOKEN_PREFIX;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthenticationService userAuthenticationService;

    private final JwtProvider jwtProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final BCryptPasswordEncoder encoder;

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        var authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        var loginUser = userAuthenticationService.loadUserByUsername(request.getUsername());

        var token = jwtProvider.generateToken(loginUser, Duration.ofHours(1));
        var httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTHORIZATION, TOKEN_PREFIX + token);

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("/api/auth-test")
    public ResponseEntity<String> authTest(@AuthenticationPrincipal User user) {
        String result = "인증된 사용자: " + user.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
