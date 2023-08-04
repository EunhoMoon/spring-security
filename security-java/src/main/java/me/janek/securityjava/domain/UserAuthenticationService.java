package me.janek.securityjava.domain;

import lombok.RequiredArgsConstructor;
import me.janek.securityjava.common.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

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

}
