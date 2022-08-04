package com.example.users.service;

import com.example.users.dto.RegistrationUserDto;
import com.example.users.entity.RoleEntity;
import com.example.users.entity.UserEntity;
import com.example.users.exception.UserAlreadyExistException;
import com.example.users.repository.RoleRepository;
import com.example.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User found in database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        log.info("Saving new user: {} to database", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        log.info("Saving new role:{} to database", role.getRoleName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Add role:{} to user:{}", roleName, username);
        UserEntity user = userRepository.findByUsername(username);
        RoleEntity role = roleRepository.findByRoleName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public UserEntity getUser(String username) {
        UserEntity user = userRepository.findByUsername(username);
        log.info("Getting user:{}", username);
        return user;
    }

    @Override
    public List<UserEntity> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        log.info("User:{} is deleted", userRepository.findById(id).get().getUsername());
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUsers(List<Long> ids) {
        log.info("Users deleted ids: {}", String.join(", ", (CharSequence) ids));
        if (isNotEmpty(ids)) {
            userRepository.deleteAllByIdIn(ids);
        }
    }

    @Override
    public void blockUser(Long id) {
        log.info("User with id:{} is blocked", id);
        UserEntity user = userRepository.findById(id).get();
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void blockUsers(List<Long> ids) {
        log.info("Users blocked ids: {}", String.join(", ", (CharSequence) ids));
        if (isNotEmpty(ids)) {
            ids.forEach(id -> {
                UserEntity user = userRepository.findById(id).get();
                user.setEnabled(false);
                userRepository.save(user);
            });
        }
    }

    @Override
    public void unblockUser(Long id) {
        log.info("User with id-{} is unblocked", id);
        UserEntity user = userRepository.findById(id).get();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUsers(List<Long> ids) {
        log.info("Users unblocked ids: {}", String.join(", ", (CharSequence) ids));
        if (isNotEmpty(ids)) {
            ids.forEach(id -> {
                UserEntity user = userRepository.findById(id).get();
                user.setEnabled(true);
                userRepository.save(user);
            });
        }
    }

    @Override
    public Optional<UserEntity> registration(RegistrationUserDto userDto) throws UserAlreadyExistException {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new UserAlreadyExistException("Пользователь с таким именем уже существует");
        }
        return Optional.ofNullable(userDto)
                .map(u -> {
                    final UserEntity user = new UserEntity(
                            u.getUsername(),
                            u.getPassword(),
                            u.getEmail()
                    );
                    user.setPassword(encoder.encode(userDto.getPassword()));
                    user.setDateRegistration(LocalDateTime.now());
                    user.setLastLogin(LocalDateTime.now());
                    userRepository.save(user);
                    addRoleToUser(user.getUsername(), "ROLE_USER");
                    return user;
                });
    }
}
