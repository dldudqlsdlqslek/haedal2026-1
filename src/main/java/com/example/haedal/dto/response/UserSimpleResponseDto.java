package com.example.haedal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSimpleResponseDto {
    private Long id;
    private String username;
    private String name;
    private String imageData;
    private Boolean isFollowing;
}
