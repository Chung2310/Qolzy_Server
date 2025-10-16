package com.example.Qolzy.mapper;

import com.example.Qolzy.model.reel.Reel;
import com.example.Qolzy.model.reel.ReelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ReelMapper.class})
public interface ReelMapper {
    @Mapping(source = "user", target = "user")
    ReelResponse toReelResponse(Reel reel);

    List<ReelResponse> toReelResponses(List<Reel> reelList);
}
