package me.dwamuya.fakeddit.repositories;

import me.dwamuya.fakeddit.models.Post;
import me.dwamuya.fakeddit.models.Subreddit;
import me.dwamuya.fakeddit.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findAllByAuthor(User author);
    Optional<List<Post>> findAllBySubreddit(Subreddit subreddit);
}