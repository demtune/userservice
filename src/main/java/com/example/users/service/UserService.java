package com.example.users.service;

import com.example.users.dto.RegistrationUserDto;
import com.example.users.entity.RoleEntity;
import com.example.users.entity.UserEntity;
import com.example.users.exception.UserAlreadyExistException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity saveUser(UserEntity user);

    RoleEntity saveRole(RoleEntity roleEntity);

    void addRoleToUser(String username, String roleName);

    UserEntity getUser(String username);

    List<UserEntity> getUsers();

    void deleteUser(Long id);

    void deleteUsers(List<Long> ids);

    void blockUser(Long id);

    void blockUsers(List<Long> ids);

    void unblockUser(Long id);

    void unblockUsers(List<Long> ids);

    Optional<UserEntity> registration(RegistrationUserDto userDto) throws UserAlreadyExistException;
}
