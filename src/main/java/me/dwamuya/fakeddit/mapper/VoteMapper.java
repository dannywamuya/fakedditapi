package me.dwamuya.fakeddit.mapper;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.VoteRequest;
import me.dwamuya.fakeddit.dto.VoteResponse;
import me.dwamuya.fakeddit.models.Post;
import me.dwamuya.fakeddit.models.Vote;
import me.dwamuya.fakeddit.security.AuthStatus;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class VoteMapper {

    private final AuthStatus authStatus;

    private final UserMapper userMapper;
    private final PostMapper postMapper;

    public Vote builder(VoteRequest voteRequest, Post post) {
        return Vote.builder()
                .post(post)
                .author(authStatus.getUser())
                .voteType(voteRequest.getVoteType())
                .build();
    }

    public Vote mapper(VoteRequest voteRequest, Vote vote) {
        vote.setVoteType(voteRequest.getVoteType());
        return vote;
    }

    public VoteResponse toResponse(Vote vote) {
        VoteResponse voteResponse = new VoteResponse();
        voteResponse.setVoteId(vote.getVoteId());
        voteResponse.setVoteType(vote.getVoteType());
        voteResponse.setAuthor(userMapper.toResponse(vote.getAuthor()));
        voteResponse.setPost(postMapper.toResponse(vote.getPost()));

        return voteResponse;
    }

}