package com.example.haedal.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailResponseDto {
    private Long id;
    private String username;
    private String name;
    private String imageData;
    private Boolean isFollowing;
    private String bio;
    private String joinedAt;
    private Long postCount;
    private Long followerCount;
    private Long followingCount;
}
