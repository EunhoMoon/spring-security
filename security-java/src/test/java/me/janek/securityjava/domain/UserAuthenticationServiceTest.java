package me.janek.securityjava.domain;

import me.janek.securityjava.interfaces.TokenRefreshRequest;
import me.janek.securityjava.interfaces.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserAuthenticationServiceTest {

    @Autowired
    UserAuthenticationService userAuthenticationService;
    
    @Test
    @DisplayName("")
     void test() {
        //given
        User user = userAuthenticationService.loadUserByUsername("janek");
        TokenResponse token = userAuthenticationService.createToken(user);

        // when
        String accessToken = userAuthenticationService.refreshAccessToken(new TokenRefreshRequest(user.getUserToken(), token.refreshToken()));

        // then
        System.out.println("accessToken = " + accessToken);
    }
    
}