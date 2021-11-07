package me.dwamuya.fakeddit.mapper;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.CommentRequest;
import me.dwamuya.fakeddit.dto.CommentResponse;
import me.dwamuya.fakeddit.models.Comment;
import me.dwamuya.fakeddit.models.Post;
import me.dwamuya.fakeddit.security.AuthStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@AllArgsConstructor
@Component
public class CommentMapper {

    private final AuthStatus authStatus;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    public Comment builder(CommentRequest commentRequest, Post post) {
        return Comment.builder()
                .body(commentRequest.getBody())
                .createdAt(Instant.now())
                .author(authStatus.getUser())
                .post(post)
                .build();
    }

    public Comment mapper(Comment comment, CommentRequest commentRequest) {
        comment.setBody(commentRequest.getBody());

        return comment;
    }

    public CommentResponse toResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setCommentId(comment.getCommentId());
        commentResponse.setAuthor(userMapper.toResponse(comment.getAuthor()));
        commentResponse.setBody(comment.getBody());
        commentResponse.setVoteCount(comment.getVoteCount());
        commentResponse.setPost(postMapper.toResponse(comment.getPost()));
        commentResponse.setCreatedAt(comment.getCreatedAt());

        return commentResponse;
    }

}
