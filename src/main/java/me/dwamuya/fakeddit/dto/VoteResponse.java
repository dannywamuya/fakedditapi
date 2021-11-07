package me.dwamuya.fakeddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dwamuya.fakeddit.models.Vote.VoteType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponse {

    private Long voteId;
    private UserResponse author;
    private VoteType voteType;
    private PostResponse post;

}