package me.janek.securityjava.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import me.janek.securityjava.domain.User;
import me.janek.securityjava.domain.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

import static me.janek.securityjava.common.JwtValidationStatus.*;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private final UserRepository userRepository;

    private final Key SECRET_KEY;

    public JwtProvider(JwtProperties jwtProperties, UserRepository userRepository) {
        this.jwtProperties = jwtProperties;
        this.userRepository = userRepository;
        var keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user, Duration expiredAt) {
        var now = new Date();
        return createToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String createToken(Date expiry, User user) {
        var now = new Date();

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(jwtProperties.getIssuer())
            .setIssuedAt(now)
            .setExpiration(expiry)
            .setSubject(user.getUsername())
            .claim("userToken", user.getUserToken())
            .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
            .compact();
    }

    public JwtValidationStatus validToken(String token) {
        try {
            getClaims(token);
        } catch (SecurityException | MalformedJwtException e) {
            return INVALID_SIGNATURE;
        } catch (ExpiredJwtException e) {
            return EXPIRED;
        } catch (UnsupportedJwtException e) {
            return NOT_SUPPORTED;
        } catch (IllegalArgumentException e) {
            return INVALID_TOKEN;
        }
        return VALID;
    }

    @Transactional
    public Authentication getAuthentication(String token) {
        var userToken = getUserToken(token);
        var user = userRepository.findUserByUserToken(userToken).orElseThrow(UserNotFoundException::new);
        var authorities = user.getAuthorities();

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    public String getUserToken(String token) {
        return getClaims(token).getBody().get("userToken", String.class);
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token);
    }

}
