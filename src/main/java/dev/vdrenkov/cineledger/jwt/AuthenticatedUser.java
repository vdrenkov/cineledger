package dev.vdrenkov.cineledger.jwt;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

public record AuthenticatedUser(String username, String password, List<SimpleGrantedAuthority> authorities,
                                long credentialsVersion) implements UserDetails {
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }
}
