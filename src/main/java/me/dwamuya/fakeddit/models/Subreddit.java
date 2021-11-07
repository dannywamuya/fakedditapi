package me.dwamuya.fakeddit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "subreddits")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subredditId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String url;

    private String description;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private User owner;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToMany(mappedBy = "subreddits", fetch = FetchType.LAZY)
    private List<User> members;

}