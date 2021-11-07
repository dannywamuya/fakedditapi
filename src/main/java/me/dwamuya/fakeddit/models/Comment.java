package me.dwamuya.fakeddit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String body;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private User author;

    @Column(nullable = false)
    private Instant createdAt;

    private int voteCount;

    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name = "postId", referencedColumnName = "postId", nullable = false)
    private Post post;


}