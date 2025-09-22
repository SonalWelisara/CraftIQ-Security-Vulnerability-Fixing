package com.CraftIQ.CraftIQ.service;

import com.CraftIQ.CraftIQ.dto.SkillPostsDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SkillPostsService {

    // Create a new SkillPost
    public SkillPostsDto postSkillPost(SkillPostsDto skillPostsDto, MultipartFile image) throws IOException;

    // Get all SkillPosts
    public List<SkillPostsDto> getAllSkillPosts();

    // Get a SkillPost by ID
    public SkillPostsDto getSkillPostById(Long id);

    // Update a SkillPost by ID
    public SkillPostsDto updateSkillPost(Long id, SkillPostsDto skillPostsDto);

    // Delete a SkillPost by ID
    public Boolean deleteSkillPost(Long id);
}

