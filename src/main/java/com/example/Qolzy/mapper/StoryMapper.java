package com.example.Qolzy.mapper;

import com.example.Qolzy.model.story.Story;
import com.example.Qolzy.model.story.StoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PostMediaMapper.class})
public interface StoryMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(source = "medias", target = "medias")
    StoryResponse toStoryResponse(Story story);

    List<StoryResponse> toStoryResponseList(List<Story> storyList);
}
