package me.janek.securityjava.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.janek.securityjava.common.TokenGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Entity(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var roleNames = this.authorities.stream()
            .map(authority -> authority.getAuthority().name())
            .toList();

        return AuthorityUtils.createAuthorityList(roleNames);
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals(Status.ACTIVE);
    }

}
