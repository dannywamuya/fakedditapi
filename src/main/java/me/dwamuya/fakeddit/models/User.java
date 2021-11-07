package me.dwamuya.fakeddit.models;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String fName;
    private String lName;
    private Boolean active;
    private String authorities = "ROLE_USER";

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_subreddits",
            joinColumns = {
                    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false, updatable = false),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "subredditId", referencedColumnName = "subredditId", nullable = false, updatable = false),
            }
    )
    private List<Subreddit> subreddits;

}