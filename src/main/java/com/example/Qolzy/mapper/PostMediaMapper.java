package com.example.Qolzy.mapper;

import com.example.Qolzy.dto.PostMediaDTO;
import com.example.Qolzy.model.post.PostMedia;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMediaMapper {
    PostMediaDTO toPostMediaDTO(PostMedia postMedia);
    PostMedia toPostMedia(PostMediaDTO postMediaDTO);

    List<PostMediaDTO> toPostMediaDTOList(List<PostMedia> postMediaList);
    List<PostMedia> toPostMediaList(List<PostMediaDTO> postMediaDTOList);
}
