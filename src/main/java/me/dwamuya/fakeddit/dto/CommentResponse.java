package me.dwamuya.fakeddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long commentId;
    private String body;
    private UserResponse author;
    private int voteCount;
    private PostResponse post;
    private Instant createdAt;

}