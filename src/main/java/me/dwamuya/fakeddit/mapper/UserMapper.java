package me.dwamuya.fakeddit.mapper;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.UserResponse;
import me.dwamuya.fakeddit.models.Subreddit;
import me.dwamuya.fakeddit.models.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@AllArgsConstructor
@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setSubredditIds(
                user.getSubreddits().stream().map(Subreddit::getSubredditId).collect(Collectors.toList())
        );
        userResponse.setSubredditCount(
                user.getSubreddits().size()
        );

        return userResponse;
    }

}