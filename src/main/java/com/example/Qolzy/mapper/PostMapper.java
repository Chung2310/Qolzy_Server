package com.example.Qolzy.mapper;

import com.example.Qolzy.dto.PostDTO;
import com.example.Qolzy.model.post.Post;
import com.example.Qolzy.model.post.PostReponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PostMediaMapper.class, UserMapper.class})
public interface PostMapper {
    PostDTO toPostDTO(Post post);
    Post toPost(PostDTO postDTO);

    List<PostDTO> toPostDTOList(List<Post> postList);
    List<Post> toPostList(List<PostDTO> postDTOList);

    @Mapping(source = "medias", target = "medias")
    @Mapping(source = "user", target = "user")
    PostReponse toPostReponse(Post post);

    List<PostReponse> toPostReponseList(List<Post> postList);
}
