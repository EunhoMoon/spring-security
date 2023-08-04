package me.janek.securityjava.common;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.janek.securityjava.domain.User;
import me.janek.securityjava.domain.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final UserRepository userRepository;

        private final BCryptPasswordEncoder encoder;

        public void dbInit() {
            var encodedPassword = encoder.encode("1234");
            var user = User.builder()
                .username("janek")
                .password(encodedPassword)
                .build();

            userRepository.save(user);
        }

    }


}
