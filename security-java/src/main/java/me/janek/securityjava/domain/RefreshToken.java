package me.janek.securityjava.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userToken;

    @Column(length = 500)
    private String refreshToken;

    @Builder
    private RefreshToken(String userToken, String refreshToken) {
        this.userToken = userToken;
        this.refreshToken = refreshToken;
    }

    public void update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }

    public boolean isNotValid(String refreshToken) {
        return !this.refreshToken.equals(refreshToken);
    }

}
