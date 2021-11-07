package me.dwamuya.fakeddit.controllers;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.SubredditRequest;
import me.dwamuya.fakeddit.mapper.SubredditMapper;
import me.dwamuya.fakeddit.models.Subreddit;
import me.dwamuya.fakeddit.services.SubredditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subreddits")
@AllArgsConstructor
@CrossOrigin
public class SubredditController {

    private final SubredditService subredditService;
    private final SubredditMapper subredditMapper;

    @PostMapping
    public ResponseEntity<?> createSubreddit(
            @RequestBody SubredditRequest subredditRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                  subredditService.createSubreddit(subredditRequest)
                );
    }

    @PostMapping("/{subredditId}/join")
    public ResponseEntity<?> joinSubreddit(
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        subredditService.joinSubreddit(subredditId)
                );
    }

    @DeleteMapping("/{subredditId}/leave")
    public ResponseEntity<?> leaveSubreddit(
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        subredditService.leaveSubreddit(subredditId)
                );
    }

    @GetMapping
    public ResponseEntity<?> findAllSubreddits() {
        return ResponseEntity.ok(subredditService.getAllSubredditResponses());
    }

    @GetMapping("/{subredditId}/members")
    public ResponseEntity<?> findAllMembers(
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity.ok(subredditService.getAllMembers(subredditId));
    }

    @GetMapping("/{subredditId}")
    public ResponseEntity<?> findOneSubreddit(
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        subredditService.getSubredditResponse(subredditId)
                );
    }

    @GetMapping("by-url/{url}")
    public ResponseEntity<?> findOneSubredditByUrl(
            @PathVariable(name = "url") String url
    ) {
        Subreddit subreddit = subredditService.findSubredditByUrl(url);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        subredditService.getSubredditResponse(subreddit)
                );
    }

    @GetMapping("/user")
    public ResponseEntity<?> findUserSubreddits() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                  subredditService.getUserSubreddits()
                );
    }

    @GetMapping("/user/non-member")
    public ResponseEntity<?> findNonMemberSubs() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    subredditService.getNonMemberSubs()
                );
    }

    @PutMapping("/{subredditId}")
    public ResponseEntity<?> updateSubreddit(
            @RequestBody SubredditRequest subredditRequest,
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        subredditService.updateSubreddit(subredditRequest, subredditId)
                );
    }

    @DeleteMapping("/{subredditId}")
    public ResponseEntity<?> deleteSubreddit(
            @PathVariable(name = "subredditId") Long subredditId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        subredditService.deleteSubreddit(subredditId)
                );
    }
}