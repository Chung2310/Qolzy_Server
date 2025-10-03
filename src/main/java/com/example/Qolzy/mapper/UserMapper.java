package com.example.Qolzy.mapper;

import com.example.Qolzy.dto.UserEntityDTO;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.auth.LoginResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntityDTO toUserEntityDTO(UserEntity userEntity);
    UserEntity toUserEntity(UserEntityDTO userEntityDTO);

    List<UserEntityDTO> toUserEntityDTOList(List<UserEntity> userEntityList);
    List<UserEntity> toUserEntityList(List<UserEntityDTO> userEntityDTOList);

    LoginResponse toLoginResponse(UserEntity userEntity);
}
