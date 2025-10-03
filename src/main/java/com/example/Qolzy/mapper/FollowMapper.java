package com.example.Qolzy.mapper;

import com.example.Qolzy.model.follow.Follow;
import com.example.Qolzy.model.follow.FollowResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class})
public interface FollowMapper {
    @Mapping(source = "following", target = "following")
    FollowResponse toFollowResponse(Follow follow);
    List<FollowResponse> toFollowResponseList(List<Follow> follows);
}
