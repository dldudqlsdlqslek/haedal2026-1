package com.example.haedal.dto.request;


import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private String username;
    private String password;
    private String name;
    private String bio;
}