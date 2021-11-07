package me.dwamuya.fakeddit.security;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.exceptions.CustomException;
import me.dwamuya.fakeddit.models.User;
import me.dwamuya.fakeddit.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AuthStatus {

    private final UserRepository userRepository;

    public User getUser() {
        return userRepository
                .findByUsername(getAuthentication().getName())
                .orElseThrow(() ->
                        new CustomException("Unable to find user of username "
                                + getAuthentication().getName()));
    }

    @Bean
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}