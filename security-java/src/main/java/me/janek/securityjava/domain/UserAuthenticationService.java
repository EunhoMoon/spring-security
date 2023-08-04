package me.janek.securityjava.domain;

import lombok.RequiredArgsConstructor;
import me.janek.securityjava.common.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User findUser = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        return new UserAuthenticationDetails(findUser);
    }

}
