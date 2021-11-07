package me.dwamuya.fakeddit.models;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String postTitle;

    @Lob
    @Column(nullable = false)
    private String description;

    private Integer voteCount;
    private Integer commentCount;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false, updatable = false)
    private User author;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne(targetEntity = Subreddit.class)
    @JoinColumn(name = "subredditId", referencedColumnName = "subredditId", nullable = false)
    private Subreddit subreddit;

    @OneToMany(mappedBy = "post")
    private List<Vote> votes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

}