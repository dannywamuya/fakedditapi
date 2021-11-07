package me.dwamuya.fakeddit.services;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.exceptions.CustomException;
import me.dwamuya.fakeddit.models.User;
import me.dwamuya.fakeddit.repositories.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new CustomException("Unable to find user of Id : " + userId)
                );
    }

    public User findUserByUsername(String username) {
            return userRepository
                    .findByUsername(username)
                    .orElseThrow(() ->
                            new CustomException("Unable to find user of username : " + username)
                    );
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
