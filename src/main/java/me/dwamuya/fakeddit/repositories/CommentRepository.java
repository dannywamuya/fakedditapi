package me.dwamuya.fakeddit.repositories;

import me.dwamuya.fakeddit.models.Comment;
import me.dwamuya.fakeddit.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findAllByPost(Post post);
}