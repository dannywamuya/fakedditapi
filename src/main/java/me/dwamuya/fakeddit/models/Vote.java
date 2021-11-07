package me.dwamuya.fakeddit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false, updatable = false)
    private User author;

    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name = "postId", referencedColumnName = "postId", nullable = false)
    private Post post;

    @Column(nullable = false)
    private VoteType voteType;

    public enum VoteType {
        UP, DOWN, NON
    }

}