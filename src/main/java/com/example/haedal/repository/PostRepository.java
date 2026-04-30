package com.example.haedal.repository;


import com.example.haedal.domain.Post;
import com.example.haedal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Long countByUser(User user);
    List<Post> findByUser(User user);
}
