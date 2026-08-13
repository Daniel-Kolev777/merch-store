package com.merchstore.services;

import com.merchstore.dtos.user.UserOutDto;
import com.merchstore.dtos.user.UserRegisterInDto;
import com.merchstore.dtos.user.UserRestoreDto;
import com.merchstore.dtos.user.UserUpdateDto;
import com.merchstore.exceptions.EntityDuplicateException;
import com.merchstore.exceptions.EntityNotFoundException;
import com.merchstore.helpers.UserMapper;
import com.merchstore.models.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.merchstore.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(UserRegisterInDto registerInDto) {
        User newUser = userMapper.fromRegisterInDtoToUser(registerInDto);
        validateUserUniqueness(newUser.getUsername(), newUser.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerInDto.getPassword()));
        userRepository.save(newUser);

    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User", "username", username));
    }

    @Override
    public UserOutDto getCurrentUser(String username) {
        User currentUser = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userMapper.fromUserToOutDto(currentUser);
    }

    @Override
    public UserOutDto updateProfile(UserUpdateDto dto, String username) {
        User userToUpdate = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            userToUpdate.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            userToUpdate.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            userToUpdate.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(userToUpdate);

        return userMapper.fromUserToOutDto(userToUpdate);
    }

    @Override
    public void deleteCurrentUser(String username) {

        User currentUser = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        currentUser.setDeleted(true);
        currentUser.setDeletedAt(LocalDateTime.now());

        userRepository.save(currentUser);
    }

    @Override
    public void restoreAccount(UserRestoreDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        if (!user.isDeleted()) {
            throw new IllegalStateException("User is already active");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        user.setDeleted(false);
        user.setDeletedAt(null);

        userRepository.save(user);
    }

    private void validateUserUniqueness(String username, String email) {
        if (userRepository.existsByUsernameAndDeletedFalse(username)) {
            throw new EntityDuplicateException("User", "username", username);
        }
        if (userRepository.existsByEmailAndDeletedFalse(email)) {
            throw new EntityDuplicateException("User", "email", email);
        }
    }
}
