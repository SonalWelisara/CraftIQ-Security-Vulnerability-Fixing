package com.CraftIQ.CraftIQ.service;

import com.CraftIQ.CraftIQ.dto.LoginRequestDto;
import com.CraftIQ.CraftIQ.dto.LoginResponseDto;
import com.CraftIQ.CraftIQ.dto.UserDto;
import com.CraftIQ.CraftIQ.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto, MultipartFile image) throws IOException;
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    UserDto getUserByUsername(String username);
    UserDto getUserByEmail(String email);
    LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto);
    User findById(Long id);

}

