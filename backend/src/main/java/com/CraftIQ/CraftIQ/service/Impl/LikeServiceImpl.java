package com.CraftIQ.CraftIQ.service.Impl;

import com.CraftIQ.CraftIQ.dto.LikeDto;
import com.CraftIQ.CraftIQ.entity.Like;
import com.CraftIQ.CraftIQ.entity.SkillPosts;
import com.CraftIQ.CraftIQ.entity.User;
import com.CraftIQ.CraftIQ.repository.LikeRepository;
import com.CraftIQ.CraftIQ.repository.SkillPostsRepository;
import com.CraftIQ.CraftIQ.repository.UserRepository;
import com.CraftIQ.CraftIQ.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final SkillPostsRepository skillPostsRepository;
    private final UserRepository userRepository;

    @Override
    public LikeDto likePost(Long postId, Long userId) {
        if (likeRepository.existsBySkillPostIdAndUserId(postId, userId)) {
            throw new RuntimeException("Already liked");
        }

        SkillPosts post = skillPostsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like = new Like();
        like.setSkillPost(post);
        like.setUser(user);
        Like saved = likeRepository.save(like);

        return saved.toDto(new ModelMapper());
    }

    @Override
    public void unlikePost(Long postId, Long userId) {
        Like like = likeRepository.findBySkillPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        likeRepository.delete(like);
    }

    @Override
    public boolean hasUserLiked(Long postId, Long userId) {
        return likeRepository.existsBySkillPostIdAndUserId(postId, userId);
    }

    @Override
    public int getLikeCount(Long postId) {
        return likeRepository.countBySkillPostId(postId);
    }
}
