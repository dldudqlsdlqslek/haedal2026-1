package com.example.haedal.service;


import com.example.haedal.domain.User;
import com.example.haedal.dto.request.UserUpdateRequestDto;
import com.example.haedal.dto.response.UserDetailResponseDto;
import com.example.haedal.dto.response.UserSimpleResponseDto;
import com.example.haedal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserSimpleResponseDto saveUser(User newUser) {
        // 중복 회원 검증
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new IllegalStateException("중복되는 username입니다.");
        }

        userRepository.save(newUser);
        return convertUserToSimpleDto(newUser, newUser);
    }

    public List<UserSimpleResponseDto> getAllUsers(User currentUser) {
        List<User> users = userRepository.findAll();
        users.remove(currentUser);
        return users.stream().map(user -> convertUserToSimpleDto(user, user)).toList();
    }


    public List<UserSimpleResponseDto> getUserByUsername(User currentUser, String username) {
        List<UserSimpleResponseDto> user = new ArrayList<>();
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetUser != null) {
            UserSimpleResponseDto userSimpleResponseDto = convertUserToSimpleDto(targetUser, targetUser);
            user.add(userSimpleResponseDto);
        }

        return user;
    }


    public UserDetailResponseDto updateUser(User currentUser, UserUpdateRequestDto userUpdateRequestDto) {
        if (userUpdateRequestDto.getUsername() != null) {
            currentUser.setUsername(userUpdateRequestDto.getUsername());
        }
        if (userUpdateRequestDto.getPassword() != null) {
            currentUser.setPassword(userUpdateRequestDto.getPassword());
        }
        if (userUpdateRequestDto.getName() != null) {
            currentUser.setName(userUpdateRequestDto.getName());
        }
        if (userUpdateRequestDto.getBio() != null) {
            currentUser.setBio(userUpdateRequestDto.getBio());
        }

        userRepository.save(currentUser);

        return convertUserToDetailDto(currentUser, currentUser);
    }


    public UserDetailResponseDto getUserDetail(User currentUser, Long targetUserId) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return convertUserToDetailDto(currentUser, targetUser);
    }


    public UserDetailResponseDto convertUserToDetailDto(User currentUser, User targetUser) {
        return new UserDetailResponseDto(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getName(),
                null,
                false,
                targetUser.getBio(),
                targetUser.getJoinedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")),
                0L,
                0L,
                0L
        );
    }




    public UserSimpleResponseDto convertUserToSimpleDto(User currentUser, User targetUser) {
        return new UserSimpleResponseDto(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getName(),
                null,
                false
                );
    }
}
