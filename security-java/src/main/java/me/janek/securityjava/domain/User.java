package me.janek.securityjava.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.janek.securityjava.common.TokenGenerator;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private final static String TOKEN_PREFIX = "USER-";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userToken;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn(name = "USER_ID")
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<Authority> authorities = new ArrayList<>();

    @Builder
    private User(
        String username,
        String password
    ) {
        this.userToken = TokenGenerator.generateToken(TOKEN_PREFIX);
        this.username = username;
        this.password = password;
        this.status = Status.ACTIVE;
        this.authorities.add(new Authority(Role.USER));
    }

    public boolean isActive() {
        return this.status.equals(Status.ACTIVE);
    }

}
