package me.dwamuya.fakeddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubredditResponse {

    private Long subredditId;
    private String name;
    private String url;
    private String description;
    private UserResponse owner;
    private int memberCount;

}