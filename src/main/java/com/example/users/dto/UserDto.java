package com.example.users.dto;

import com.example.users.entity.UserEntity;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String email;

    public static UserDto toModel(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
        userDto.setEmail(userEntity.getEmail());
        return userDto;
    }
}
