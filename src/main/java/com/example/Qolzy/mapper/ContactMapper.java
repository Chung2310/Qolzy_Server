package com.example.Qolzy.mapper;

import com.example.Qolzy.model.contact.Contact;
import com.example.Qolzy.model.contact.ContactResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ContactMapper {
    @Mapping(source = "user", target = "userContact")
    ContactResponse toResponse(Contact contact);

    List<ContactResponse> toResponseList(List<Contact> contacts);
}
