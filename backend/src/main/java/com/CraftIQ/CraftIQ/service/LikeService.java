package com.CraftIQ.CraftIQ.service;

import com.CraftIQ.CraftIQ.dto.LikeDto;

public interface LikeService {
    LikeDto likePost(Long postId, Long userId);
    void unlikePost(Long postId, Long userId);
    boolean hasUserLiked(Long postId, Long userId);
    int getLikeCount(Long postId);
}
