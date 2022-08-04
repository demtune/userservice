package com.example.users.repository;

import com.example.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    void deleteAllByIdIn(List<Long> ids);

    void findAllByIdIn(List<Long> ids);
}
