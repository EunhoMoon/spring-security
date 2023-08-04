package me.janek.securityjava.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserToken(String userToken);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
