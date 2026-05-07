package com.example.haedal.controller;


import com.example.haedal.domain.Post;
import com.example.haedal.domain.User;
import com.example.haedal.dto.response.PostResponseDto;
import com.example.haedal.dto.response.UserSimpleResponseDto;
import com.example.haedal.service.AuthService;
import com.example.haedal.service.ImageService;
import com.example.haedal.service.LikeService;
import com.example.haedal.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class PostController {
    private final AuthService authService;
    private final ImageService imageService;
    private final PostService postService;
    private final LikeService likeService;

    @Autowired
    public PostController(AuthService authService, ImageService imageService, PostService postService, LikeService likeService) {
        this.authService = authService;
        this.imageService = imageService;
        this.postService = postService;
        this.likeService = likeService;
    }


    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestParam("image") MultipartFile image, @RequestParam("content") String content,
                                           HttpServletRequest request) throws IOException {
        User currentUser = authService.getCurrentUser(request);
        String imageUrl = imageService.savePostImage(image);
        Post post = new Post(currentUser, content, imageUrl);
        postService.savePost(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(@PathVariable Long userId) {
        List<PostResponseDto> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }


    @PostMapping("posts/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        likeService.likePost(currentUser, postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("posts/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        likeService.unlikePost(currentUser, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("posts/{postId}/like")
    public ResponseEntity<List<UserSimpleResponseDto>> getUsersWhoLikedPost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        List<UserSimpleResponseDto> usersWhoLikedPost = likeService.getUsersWhoLikedPost(currentUser, postId);
        return ResponseEntity.ok(usersWhoLikedPost);
    }

}
