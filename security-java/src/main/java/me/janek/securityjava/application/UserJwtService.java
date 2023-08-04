package me.janek.securityjava.application;

import lombok.RequiredArgsConstructor;
import me.janek.securityjava.common.JwtProvider;
import me.janek.securityjava.domain.UserAuthenticationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserJwtService {

    private final JwtProvider jwtProvider;

    private final UserAuthenticationService userAuthenticationService;

//    public String createNewAccessToken(String refreshToken) {
//        if (!jwtProvider.validToken(refreshToken)) throw new IllegalArgumentException("Unexpected Token");
//
//        String userToken = userAuthenticationService.getRefreshToken(refreshToken).getUserToken();
//        User findUser = userAuthenticationService.findByUserToken(userToken);
//
//        return jwtProvider.generateToken(findUser, Duration.ofHours(2));
//    }

}
