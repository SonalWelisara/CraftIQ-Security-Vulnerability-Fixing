package com.CraftIQ.CraftIQ.controller;

import com.CraftIQ.CraftIQ.dto.LoginRequestDto;
import com.CraftIQ.CraftIQ.dto.LoginResponseDto;
import com.CraftIQ.CraftIQ.dto.UserDto;
import com.CraftIQ.CraftIQ.entity.User;
import com.CraftIQ.CraftIQ.service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserServiceImpl userService;

    // Create User
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<UserDto> createUser(
            @RequestPart("user") UserDto userDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            UserDto savedUser = userService.createUser(userDto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Get all Users
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    // Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    // Update User by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, userDto));
    }

    // Delete User by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // or .ok().build() if you prefer
    }


    // Get User by Username
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUsername(username));
    }

    // Get User by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(email));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginDto) {
        LoginResponseDto response = userService.authenticateUser(loginDto);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null || user.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = "image/jpeg"; // fallback
        try {
            // Use .jpg as extension to help probe detect correct MIME type
            Path tempFile = Files.createTempFile("img-", ".jpg");
            Files.write(tempFile, user.getImageData());
            String detectedType = Files.probeContentType(tempFile);
            if (detectedType != null) {
                contentType = detectedType;
            }
            Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            System.err.println("Failed to detect image type: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(user.getImageData());
    }



}
