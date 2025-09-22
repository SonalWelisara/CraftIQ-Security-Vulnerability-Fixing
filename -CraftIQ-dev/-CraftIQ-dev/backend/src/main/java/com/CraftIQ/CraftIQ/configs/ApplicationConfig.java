package com.CraftIQ.CraftIQ.configs;

import com.CraftIQ.CraftIQ.dto.UserDto;
import com.CraftIQ.CraftIQ.entity.User;
import jakarta.servlet.MultipartConfigElement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Tell ModelMapper to skip followers, following, feedbacks, and skillPosts in User to UserDto
        modelMapper.typeMap(User.class, UserDto.class).addMappings(mapper -> {
            mapper.skip(UserDto::setFollowers);
            mapper.skip(UserDto::setFollowing);
            mapper.skip(UserDto::setFeedbacks);
            mapper.skip(UserDto::setSkillPosts);
            mapper.skip(UserDto::setLearningPlans);
        });

        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setFeedbacks));
        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setLearningPlans));
        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setSkillPosts));



        return modelMapper;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        return factory.createMultipartConfig();
    }




}