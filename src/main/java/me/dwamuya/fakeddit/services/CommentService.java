package me.dwamuya.fakeddit.services;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.CommentRequest;
import me.dwamuya.fakeddit.dto.CommentResponse;
import me.dwamuya.fakeddit.exceptions.CustomException;
import me.dwamuya.fakeddit.mapper.CommentMapper;
import me.dwamuya.fakeddit.models.Comment;
import me.dwamuya.fakeddit.models.Post;
import me.dwamuya.fakeddit.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentResponse createComment(CommentRequest commentRequest, Post post) {
        Comment comment = commentMapper.builder(commentRequest, post);

        return getCommentResponse(commentRepository.save(comment));
    }

    public List<CommentResponse> findPostComments(Post post) {
        List<Comment> comments = commentRepository
                .findAllByPost(post)
                .orElseThrow(() ->
                    new CustomException("Unable to find post of Id : " + post.getPostId())
                );

        return comments
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse getCommentResponse(Comment comment) {
        return commentMapper.toResponse(comment);
    }

}