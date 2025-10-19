package com.example.Qolzy.mapper;

import com.example.Qolzy.model.message.Message;
import com.example.Qolzy.model.message.MessageRequest;
import com.example.Qolzy.model.message.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface MessageMapper {

    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "receiver", target = "receiver")
    MessageResponse toMessageResponse(Message message);

    List<MessageResponse> toMessageResponseList(List<Message> messages);
}
