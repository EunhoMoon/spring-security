package me.janek.securityjava.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "USER_ID")
    private final List<Authorities> authorities = new ArrayList<>();

    @Builder
    private User(
        Long id,
        String username,
        String password
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

}
