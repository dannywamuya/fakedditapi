package me.dwamuya.fakeddit.repositories;

import me.dwamuya.fakeddit.models.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

    Optional<Subreddit> findByUrl(String url);

}