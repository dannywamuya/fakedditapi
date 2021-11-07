package me.dwamuya.fakeddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dwamuya.fakeddit.models.Vote.VoteType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {

    private VoteType voteType;

}