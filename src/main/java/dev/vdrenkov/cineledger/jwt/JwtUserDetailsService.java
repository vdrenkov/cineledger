package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.exceptions.UserNotFoundException;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads application users for Spring Security authentication.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(JwtUserDetailsService.class);

    private final UserRepository userRepository;

    /**
     * Creates a new jwt user details service with its required collaborators.
     *
     * @param userRepository
     *     user repository used by the operation
     */
    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the security principal for the supplied username.
     *
     * @param username
     *     username to search for
     * @return requested user details value
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> {
            log.error(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
        });

        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        log.info(String.format("User with username %s loaded", username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            authorities);
    }
}

