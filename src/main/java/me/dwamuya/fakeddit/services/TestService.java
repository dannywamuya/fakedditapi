package me.dwamuya.fakeddit.services;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.UserResponse;
import me.dwamuya.fakeddit.mapper.UserMapper;
import me.dwamuya.fakeddit.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TestService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}