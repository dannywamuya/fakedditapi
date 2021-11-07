package me.dwamuya.fakeddit.mapper;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.PostRequest;
import me.dwamuya.fakeddit.dto.PostResponse;
import me.dwamuya.fakeddit.models.Post;
import me.dwamuya.fakeddit.models.Subreddit;
import me.dwamuya.fakeddit.security.AuthStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

@AllArgsConstructor
@Component
public class PostMapper {

    private final UserMapper userMapper;
    private final SubredditMapper subredditMapper;

    private final AuthStatus authStatus;

    public Post builder(PostRequest postRequest, Subreddit subreddit) {
        return Post.builder()
                .postTitle(postRequest.getPostTitle())
                .description(postRequest.getDescription())
                .createdAt(Instant.now())
                .subreddit(subreddit)
                .author(authStatus.getUser())
                .voteCount(0)
                .commentCount(0)
                .comments(new ArrayList<>())
                .votes(new ArrayList<>())
                .build();
    }

    public Post mapper(PostRequest postRequest, Post post, Subreddit subreddit) {
        post.setPostTitle(postRequest.getPostTitle());
        post.setDescription(postRequest.getDescription());
        post.setSubreddit(subreddit);

        return post;
    }

    public PostResponse toResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setPostId(post.getPostId());
        postResponse.setPostTitle(post.getPostTitle());
        postResponse.setDescription(post.getDescription());
        postResponse.setVoteCount(post.getVoteCount());
        postResponse.setCommentCount(post.getCommentCount());
        postResponse.setAuthor(userMapper.toResponse(post.getAuthor()));
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setSubreddit(subredditMapper.toResponse(post.getSubreddit()));

        return postResponse;
    }
}
