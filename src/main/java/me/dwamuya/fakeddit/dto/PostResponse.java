package me.dwamuya.fakeddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long postId;
    private String postTitle;
    private String description;
    private int voteCount;
    private UserResponse author;
    private Instant createdAt;
    private SubredditResponse subreddit;
    private int commentCount;

}