package com.example.Qolzy.mapper;

import com.example.Qolzy.model.comment.Comment;
import com.example.Qolzy.model.comment.CommentRepliesResponse;
import com.example.Qolzy.model.comment.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class})
public interface CommentMapper {
    @Mapping(source = "userComment", target = "userComment")
    @Mapping(source = "parent.id", target = "parenId")
    CommentResponse toCommentResponse(Comment comment);
    List<CommentResponse> toCommentResponseList(List<Comment> comments);

    @Mapping(source = "userComment", target = "userComment")
    CommentRepliesResponse toCommentRepliesResponse(Comment comment);
    List<CommentRepliesResponse> toCommentRepliesResponseList(List<Comment> comments);
}
