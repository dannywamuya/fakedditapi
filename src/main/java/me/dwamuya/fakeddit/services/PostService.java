package me.dwamuya.fakeddit.services;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.*;
import me.dwamuya.fakeddit.exceptions.CustomException;
import me.dwamuya.fakeddit.mapper.CommentMapper;
import me.dwamuya.fakeddit.mapper.PostMapper;
import me.dwamuya.fakeddit.mapper.VoteMapper;
import me.dwamuya.fakeddit.models.*;
import me.dwamuya.fakeddit.repositories.PostRepository;
import me.dwamuya.fakeddit.security.AuthStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    private final AuthStatus authStatus;

    private final SubredditService subredditService;
    private final UserService userService;
    private final CommentService commentService;
    private final VoteService voteService;

    private final PostMapper postMapper;
    private final VoteMapper voteMapper;
    private final CommentMapper commentMapper;

    /*  Post */

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findPostById(Long postId) {
        return postRepository
                .findById(postId)
                .orElseThrow(() ->
                        new CustomException("Unable to find post of Id : " + postId)
                );
    }

    public List<PostResponse> findPostsByAuthor(Long userId) {
        User user = userService.findUserById(userId);

        List<Post> posts = postRepository
                .findAllByAuthor(user)
                .orElseThrow(() ->
                    new CustomException("Unable to find posts by user of Id : " + userId)
                );

        return getAllPostResponses(posts);
    }

    public List<PostResponse> findPostsByAuthorUsername(String username) {
        User user = userService.findUserByUsername(username);

        List<Post> posts = postRepository
                .findAllByAuthor(user)
                .orElseThrow(() ->
                        new CustomException("Unable to find posts by user of username : " + username)
                );

        return getAllPostResponses(posts);
    }

    public List<PostResponse> findPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditService.findOneSubreddit(subredditId);

        List<Post> posts = postRepository
                .findAllBySubreddit(subreddit)
                .orElseThrow(() ->
                    new CustomException("Unable to find posts for subreddit of Id : " + subredditId)
                );

        return getAllPostResponses(posts);
    }

    public List<PostResponse> findPostsBySubreddit(String url) {
        Subreddit subreddit = subredditService.findSubredditByUrl(url);

        List<Post> posts = postRepository
                .findAllBySubreddit(subreddit)
                .orElseThrow(() ->
                        new CustomException("Unable to find posts for subreddit of Id : " + url)
                );

        return getAllPostResponses(posts);
    }

    public List<PostResponse> getPostByUsersSubreddit() {
        List<PostResponse> res = new ArrayList<>();
        authStatus
                .getUser()
                .getSubreddits()
                .forEach(subreddit -> res.addAll(findPostsBySubreddit(subreddit.getSubredditId())));

        return res;
    }

    public Object createPost(PostRequest postRequest, Long subredditId) {
        Subreddit subreddit = subredditService.findOneSubreddit(subredditId);

        if(isMember(subreddit)) {
            Post post = postRepository.save(postMapper.builder(postRequest, subreddit));
            return getPostResponse(post);   
        }

        return "You must be a member of " + subreddit.getName() + " to post." ;
    }

    public Object updatePost(PostRequest postRequest, Long postId, Long subredditId) {
        Subreddit subreddit = subredditService.findOneSubreddit(subredditId);
        Post post = postMapper.mapper(postRequest, findPostById(postId), subreddit);

        if(isAuthor(post)) {
            postRepository.save(post);
            return getPostResponse(post);
        }

        return "Illegal attempt to update post";
    }

    public Object deletePost(Long postId) {
        Post post = findPostById(postId);

        if(isAuthor(post)) {
            postRepository.delete(post);
            return "Post of Id : " + postId + " deleted";
        }

        return "Illegal attempt to delete post";
    }

    /*  Comment */

    public List<CommentResponse> findPostComments(Long postId) {
        Post post = findPostById(postId);
        return commentService.findPostComments(post);
    }

    public CommentResponse addComment(Long postId, CommentRequest commentRequest) {
        Post post = findPostById(postId);
        CommentResponse res = commentService.createComment(commentRequest, post);
        updateCommentCount(post);

        return res;
    }

    @Async
    private void updateCommentCount(Post post) {
        List<CommentResponse> comments = commentService.findPostComments(post);
        post.setCommentCount(comments.size());
        postRepository.save(post);
    }

    /*  Util */

    private boolean isAuthor(Post post) {
        return post
                .getAuthor()
                .getUserId()
                .equals(
                        authStatus.getUser().getUserId()
                );
    }

    private boolean isMember(Subreddit subreddit) {
        return subreddit.getMembers().contains(authStatus.getUser());
    }

    /* Votes */

    public Object vote(Long postId, VoteRequest voteRequest) {
        Post post = findPostById(postId);
        Vote vote = voteService.vote(voteRequest, post);

        if (vote.getVoteId() != null) {
            updateVoteCount(post, vote);
            return voteMapper.toResponse(vote);
        }

        return "Vote deleted";
    }

    @Async
    private void updateVoteCount(Post post, Vote vote) {
        List<Vote> votes = post.getVotes();

        if(!votes.contains(vote)) {
            votes.add(vote);
        }

        int up = 0, down = 0;

        for (Vote v : votes) {
            if (v.getVoteType() == Vote.VoteType.UP) {
                up += 1;
            } else if (v.getVoteType() == Vote.VoteType.DOWN) {
                down += 1;
            }
        }

        post.setVoteCount(up - down);
        postRepository.save(post);
    }

    /* Responses */

    public PostResponse getPostResponse(Post post) {
        return postMapper.toResponse(post);
    }

    public PostResponse getPostResponse(Long postId) {
        return postMapper.toResponse(findPostById(postId));
    }

    public List<PostResponse> getAllPostResponses() {
        List<Post> posts = findAllPosts();

        return posts
                .stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getAllPostResponses(List<Post> posts) {
        return posts
                .stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Object findPostVotes(Long postId) {
        return findPostById(postId)
                .getVotes()
                .stream()
                .map(voteMapper::toResponse)
                .collect(Collectors.toList());
    }

}