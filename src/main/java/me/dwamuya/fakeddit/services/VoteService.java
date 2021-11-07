package me.dwamuya.fakeddit.services;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.VoteRequest;
import me.dwamuya.fakeddit.mapper.VoteMapper;
import me.dwamuya.fakeddit.models.Post;
import me.dwamuya.fakeddit.models.Vote;
import me.dwamuya.fakeddit.repositories.VoteRepository;
import me.dwamuya.fakeddit.security.AuthStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    private final AuthStatus authStatus;

    public Vote vote(VoteRequest voteRequest, Post post) {
        Vote lookup = voteRepository
                .findByAuthorAndPost(authStatus.getUser(), post)
                .orElse(Vote.builder().voteId(null).voteType(null).build());

//        if(voteRequest.getVoteType() == Vote.VoteType.NON) {
//            voteRepository.delete(lookup);
//        } else
            if(lookup.getVoteType() != voteRequest.getVoteType()) {
            if(lookup.getVoteId() == null) {
                return voteRepository
                        .save(voteMapper.builder(voteRequest, post));
            } else {
                return voteRepository
                        .save(voteMapper.mapper(voteRequest, lookup));
            }
        }

        return Vote.builder().voteId(null).build();
    }
}