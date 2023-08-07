package me.janek.securityjava.domain;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import me.janek.securityjava.common.JwtProvider;
import me.janek.securityjava.common.UserNotFoundException;
import me.janek.securityjava.interfaces.TokenRefreshRequest;
import me.janek.securityjava.interfaces.TokenResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProvider jwtProvider;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User findByUserToken(String userToken) {
        return userRepository.findUserByUserToken(userToken).orElseThrow(UserNotFoundException::new);
    }

    public RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public TokenResponse createToken(User loginUser) {
        var accessToken = jwtProvider.generateToken(loginUser, Duration.ofSeconds(5));
        var refreshToken = jwtProvider.generateToken(loginUser, Duration.ofHours(2));

        var refreshTokenObj = refreshTokenRepository.findByUserToken(loginUser.getUserToken());

        if (refreshTokenObj.isPresent()) {
            refreshTokenObj.get().update(refreshToken);
        } else {
            RefreshToken newRefreshToken = RefreshToken.builder()
                .userToken(loginUser.getUserToken())
                .refreshToken(refreshToken)
                .build();
            refreshTokenRepository.save(newRefreshToken);
        }
        return new TokenResponse(loginUser.getUserToken(), accessToken, refreshToken);
    }

    public String refreshAccessToken(TokenRefreshRequest request) {
        var refreshTokenObj = refreshTokenRepository.findByUserToken(request.userToken()).orElseThrow(UserNotFoundException::new);

        if (!jwtProvider.validToken(request.refreshToken()) || refreshTokenObj.isNotValid(request.refreshToken())) {
            refreshTokenRepository.delete(refreshTokenObj);
            throw new MalformedJwtException("잘못된 토큰입니다.");
        }

        var user = userRepository.findUserByUserToken(request.userToken()).orElseThrow(UserNotFoundException::new);

        return createToken(user).accessToken();
    }

}
