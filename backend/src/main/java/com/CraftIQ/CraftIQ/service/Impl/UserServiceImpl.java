package com.CraftIQ.CraftIQ.service.Impl;

import com.CraftIQ.CraftIQ.configs.JwtUtil;
import com.CraftIQ.CraftIQ.dto.FeedbackDto;
import com.CraftIQ.CraftIQ.dto.LoginRequestDto;
import com.CraftIQ.CraftIQ.dto.LoginResponseDto;
import com.CraftIQ.CraftIQ.dto.UserDto;
import com.CraftIQ.CraftIQ.entity.Feedback;
import com.CraftIQ.CraftIQ.entity.SkillPosts;
import com.CraftIQ.CraftIQ.entity.User;
import com.CraftIQ.CraftIQ.exception.NotFoundException;
import com.CraftIQ.CraftIQ.repository.FeedbackRepository;
import com.CraftIQ.CraftIQ.repository.UserRepository;
import com.CraftIQ.CraftIQ.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    // Create User
    @Override
    public UserDto createUser(UserDto userDto , MultipartFile image) throws IOException {
        // 1. Convert DTO to Entity
        User user = mapper.map(userDto, User.class);

        // Handle Image Upload
        if (image != null && !image.isEmpty()) {
            user.setImageData(image.getBytes());
        }


        // 2. Handle followers
        if (userDto.getFollowers() != null && !userDto.getFollowers().isEmpty()) {
            Set<User> followers = userDto.getFollowers().stream()
                    .map(summary -> userRepository.findById(summary.getId())
                            .orElseThrow(() -> new RuntimeException("Follower not found with ID: " + summary.getId())))
                    .collect(Collectors.toSet());
            user.setFollowers(followers);
        }

        // 3. Handle following
        if (userDto.getFollowing() != null && !userDto.getFollowing().isEmpty()) {
            Set<User> following = userDto.getFollowing().stream()
                    .map(summary -> userRepository.findById(summary.getId())
                            .orElseThrow(() -> new RuntimeException("Following user not found with ID: " + summary.getId())))
                    .collect(Collectors.toSet());
            user.setFollowing(following);
        }

        // 4. Handle feedbacks (establish back-reference)
        if (userDto.getFeedbacks() != null && !userDto.getFeedbacks().isEmpty()) {
            Set<Feedback> feedbackEntities = userDto.getFeedbacks().stream()
                    .map(dto -> {
                        Feedback feedback = mapper.map(dto, Feedback.class);
                        feedback.setUser(user); // Establish the relationship
                        return feedback;
                    })
                    .collect(Collectors.toSet());

            user.setFeedbacks(feedbackEntities);
        }

        // 5. Handle skillPosts (establish relationship between user and skill posts)
        if (userDto.getSkillPosts() != null && !userDto.getSkillPosts().isEmpty()) {
            Set<SkillPosts> skillPostEntities = userDto.getSkillPosts().stream()
                    .map(dto -> {
                        SkillPosts skillPost = dto.toEntity(mapper);
                        skillPost.setUser(user); // Establish the relationship (user owns the post)
                        return skillPost;
                    })
                    .collect(Collectors.toSet());
            user.setSkillPosts(skillPostEntities); // Associate posts with the user
        }

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 5. Save Entity
        User savedUser = userRepository.save(user);

        // 6. Return DTO
        UserDto savedDto = savedUser.toDto(mapper);

        if (savedUser.getImageData() != null) {
            savedDto.setImageBase64(Base64.getEncoder().encodeToString(savedUser.getImageData()));
        }

        return savedDto;

    }



    // Get all Users
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ArrayList<>();
        } else {
            return users.stream()
                    .map(user -> user.toDto(mapper))  // this calls toDto including image conversion
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().toDto(mapper);  // this calls toDto including image conversion
        } else {
            throw new NotFoundException("User not found with ID: " + id);
        }
    }


    // Update User by ID
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));

        // Map basic fields, but skip collections like feedbacks if mapper is configured that way
        mapper.map(userDto, existingUser);

        if (userDto.getImageBase64() != null && !userDto.getImageBase64().isEmpty()) {
            existingUser.setImageData(Base64.getDecoder().decode(userDto.getImageBase64()));
        }

        if (userDto.getFollowers() != null) {
            Set<User> followers = userDto.getFollowers().stream()
                    .map(summary -> userRepository.findById(summary.getId())
                            .orElseThrow(() -> new RuntimeException("Follower not found with ID: " + summary.getId())))
                    .collect(Collectors.toSet());
            existingUser.setFollowers(followers);
        }

        if (userDto.getFollowing() != null) {
            Set<User> following = userDto.getFollowing().stream()
                    .map(summary -> userRepository.findById(summary.getId())
                            .orElseThrow(() -> new RuntimeException("Following user not found with ID: " + summary.getId())))
                    .collect(Collectors.toSet());
            existingUser.setFollowing(following);
        }

        if (userDto.getFeedbacks() != null) {
            Set<Feedback> existingFeedbacks = existingUser.getFeedbacks();

            // Create a map of existing feedbacks for fast lookup
            Map<Long, Feedback> existingFeedbackMap = existingFeedbacks.stream()
                    .filter(f -> f.getId() != null)
                    .collect(Collectors.toMap(Feedback::getId, f -> f));

            // Prepare updated set
            Set<Feedback> updatedFeedbacks = new HashSet<>();

            for (FeedbackDto dto : userDto.getFeedbacks()) {
                Feedback feedback;
                if (dto.getId() != null && existingFeedbackMap.containsKey(dto.getId())) {
                    feedback = existingFeedbackMap.get(dto.getId());
                    mapper.map(dto, feedback);
                } else if (dto.getId() != null) {
                    // fetch from DB if it's not in current collection
                    feedback = feedbackRepository.findById(dto.getId()).orElse(null);
                    if (feedback == null) {
                        feedback = mapper.map(dto, Feedback.class);
                    } else {
                        mapper.map(dto, feedback);
                    }
                } else {
                    feedback = mapper.map(dto, Feedback.class);
                }

                feedback.setUser(existingUser);
                updatedFeedbacks.add(feedback);
            }

            // Now clear and repopulate the existing collection WITHOUT reassigning the set object
            existingFeedbacks.clear();
            existingFeedbacks.addAll(updatedFeedbacks);
        }


        if (userDto.getSkillPosts() != null) {
            existingUser.getSkillPosts().clear();
            Set<SkillPosts> updatedPosts = userDto.getSkillPosts().stream()
                    .map(dto -> {
                        SkillPosts post = dto.toEntity(mapper);
                        post.setUser(existingUser);
                        return post;
                    }).collect(Collectors.toSet());
            existingUser.getSkillPosts().addAll(updatedPosts);
        }

        return userRepository.save(existingUser).toDto(mapper);
    }



    // Delete User by ID
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        // Remove the user from other users' followers and following lists
        for (User follower : user.getFollowers()) {
            follower.getFollowing().remove(user);
        }

        for (User followed : user.getFollowing()) {
            followed.getFollowers().remove(user);
        }

        user.getFollowers().clear();
        user.getFollowing().clear();

        userRepository.delete(user);
    }


    // Get User by Username
    @Override
    public UserDto getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return mapper.map(user.get(), UserDto.class);
        } else {
            throw new NotFoundException("User not found with username: " + username);
        }
    }

    // Get User by Email
    @Override
    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return mapper.map(user.get(), UserDto.class);
        } else {
            throw new NotFoundException("User not found with email: " + email);
        }
    }

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        // Find user
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        // Check password using BCrypt
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Assign role
        String role = user.getRole();
        if (role == null || role.isEmpty()) {
            role = "USER";
        }

        //  Generate token
        String token = jwtUtil.generateToken(user.getEmail());

        // Set up response DTO
        LoginResponseDto response = new LoginResponseDto();
        response.setMessage("Login successful");
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(role);
        response.setToken(token);  // Add token

        return response;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
