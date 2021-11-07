package me.dwamuya.fakeddit.mapper;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.SubredditRequest;
import me.dwamuya.fakeddit.dto.SubredditResponse;
import me.dwamuya.fakeddit.models.Subreddit;
import me.dwamuya.fakeddit.models.User;
import me.dwamuya.fakeddit.security.AuthStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Component
public class SubredditMapper {

    private final AuthStatus authStatus;
    private final UserMapper userMapper;

    public Subreddit builder(SubredditRequest subredditRequest) {
        User user = authStatus.getUser();
        List<User> members = new ArrayList<>();
        String name = subredditRequest.getName();
        String url = name.toLowerCase(Locale.ROOT).replace(' ', '_').replaceAll("[^a-zA-Z0-9/]", "");

        members.add(user);

        return Subreddit.builder()
            .name(name)
            .url(url)
            .description(subredditRequest.getDescription())
            .createdAt(Instant.now())
            .owner(user)
            .members(members)
            .build();
    }

    public Subreddit mapper(
            SubredditRequest subredditRequest,
            Subreddit subreddit
    ) {
        subreddit.setName(subredditRequest.getName());
        subreddit.setDescription(subredditRequest.getDescription());

        return subreddit;
    }

    public SubredditResponse toResponse(Subreddit subreddit) {
        SubredditResponse subredditResponse = new SubredditResponse();
        subredditResponse.setSubredditId(subreddit.getSubredditId());
        subredditResponse.setName(subreddit.getName());
        subredditResponse.setUrl(subreddit.getUrl());
        subredditResponse.setDescription(subreddit.getDescription());
        subredditResponse.setOwner(userMapper.toResponse(subreddit.getOwner()));
        subredditResponse.setMemberCount(subreddit.getMembers().size());

        return subredditResponse;
    }

}