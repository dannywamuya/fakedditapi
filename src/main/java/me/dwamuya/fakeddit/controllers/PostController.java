package me.dwamuya.fakeddit.controllers;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.CommentRequest;
import me.dwamuya.fakeddit.dto.PostRequest;
import me.dwamuya.fakeddit.dto.VoteRequest;
import me.dwamuya.fakeddit.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@CrossOrigin
public class PostController {

    private final PostService postService;

    /* Posts */

    @GetMapping
    public ResponseEntity<?> findAllPosts() {
        return ResponseEntity.ok(postService.getAllPostResponses());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> findOnePost(
            @PathVariable(name = "postId") Long postId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    postService.getPostResponse(postId)
                );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findPostsByAuthor(
            @PathVariable(name = "userId") Long userId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        postService.findPostsByAuthor(userId)
                );
    }

    @GetMapping("/user/by-username/{username}")
    public ResponseEntity<?> findPostsByAuthorUsername(
            @PathVariable(name = "username") String username
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        postService.findPostsByAuthorUsername(username)
                );
    }

    @GetMapping("/user/subreddit-post")
    public ResponseEntity<?> findPostByUsersSubreddit() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        postService.getPostByUsersSubreddit()
                );
    }

    @GetMapping("/subreddit/{subredditId}")
    public ResponseEntity<?> findPostsBySubreddit(
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        postService.findPostsBySubreddit(subredditId)
                );
    }

    @GetMapping("/subreddit/by-url/{url}")
    public ResponseEntity<?> findPostsBySubreddit(
            @PathVariable(name = "url") String url
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        postService.findPostsBySubreddit(url)
                );
    }

    @PostMapping("/subreddit/{subredditId}")
    public ResponseEntity<?> createPost(
            @RequestBody PostRequest postRequest,
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        postService.createPost(postRequest, subredditId)
                );
    }

    @PutMapping("/subreddit/{subredditId}/{postId}")
    public ResponseEntity<?> updatePost(
            @RequestBody PostRequest postRequest,
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    postService.updatePost(postRequest, postId, subredditId)
                );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable(name = "postId") Long postId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    postService.deletePost(postId)
                );
    }

    /* Comments */

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> findPostComments(
            @PathVariable(name = "postId") Long postId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        postService.findPostComments(postId)
                );
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(
            @RequestBody CommentRequest commentRequest,
            @PathVariable(name = "postId") Long postId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        postService.addComment(postId, commentRequest)
                );
    }

    /* Votes */

    @GetMapping("/{postId}/votes")
    public ResponseEntity<?> findPostVotes(
            @PathVariable(name = "postId") Long postId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        postService.findPostVotes(postId)
                );
    }

    @PostMapping("/{postId}/votes")
    public ResponseEntity<?> vote(
            @RequestBody VoteRequest voteRequest,
            @PathVariable(name = "postId") Long postId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        postService.vote(postId, voteRequest)
                );
    }

}