package me.dwamuya.fakeddit.repositories;

import me.dwamuya.fakeddit.models.User;
import me.dwamuya.fakeddit.models.Post;
import me.dwamuya.fakeddit.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByAuthorAndPost(User author, Post post);
    Optional<List<Vote>> findAllByPost(Post post);

}