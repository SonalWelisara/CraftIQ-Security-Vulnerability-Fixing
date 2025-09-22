package com.CraftIQ.CraftIQ.dto;

import com.CraftIQ.CraftIQ.entity.Like;
import com.CraftIQ.CraftIQ.entity.SkillPosts;
import com.CraftIQ.CraftIQ.entity.User;
import lombok.Data;

@Data
public class LikeDto {
    private Long id;
    private Long userId;
    private Long skillPostId;

    public Like toEntity() {
        Like like = new Like();
        User user = new User();
        user.setId(this.userId);
        SkillPosts post = new SkillPosts();
        post.setId(this.skillPostId);
//        like.setUser(user);
        like.setSkillPost(post);
        return like;
    }

}
