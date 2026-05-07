package com.example.haedal.service;


import com.example.haedal.domain.Like;
import com.example.haedal.domain.Post;
import com.example.haedal.domain.User;
import com.example.haedal.dto.response.UserSimpleResponseDto;
import com.example.haedal.repository.LikeRepository;
import com.example.haedal.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserService userService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public void likePost(User currentUser, Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        if (likeRepository.existsByUserAndPost(currentUser, post)) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }

        Like like = new Like(currentUser, post);
        likeRepository.save(like);
    }

    public void unlikePost(User currentUser, Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        Like like = likeRepository.findByUserAndPost(currentUser, post)
                .orElseThrow(() -> new IllegalArgumentException("좋아요가 존재하지 않습니다."));
        likeRepository.delete(like);
    }

    public List<UserSimpleResponseDto> getUsersWhoLikedPost(User currentUser, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        List<Like> likes = likeRepository.findByPost(post);

        return likes.stream()
                .map(like -> userService.convertUserToSimpleDto(currentUser, like.getUser()))
                .toList();
    }


}
