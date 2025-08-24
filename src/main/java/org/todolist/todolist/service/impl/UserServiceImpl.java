package org.todolist.todolist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.todolist.todolist.dto.request.ChangePasswordRequest;
import org.todolist.todolist.dto.request.UpdateUserNameRequest;
import org.todolist.todolist.dto.response.UserResponse;
import org.todolist.todolist.entity.User;
import org.todolist.todolist.repository.UserRepository;
import org.todolist.todolist.service.UserService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("there is no user with this name"));
    }

    @Override
    public UserResponse getUserResponse(String email) {
        User user = getUserByEmail(email);
        System.out.println(user);

        return UserResponse.newUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public UserResponse updateUser(UpdateUserNameRequest updateDetails , UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new RuntimeException("user is not found"));

        user.setName(updateDetails.getName());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        return UserResponse.newUserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt()
        );
    }

    @Override
    public void deleteUserAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("something wrong with your user details"));

        userRepository.delete(user);
    }

    @Override
    public UserResponse changeUserPassword(ChangePasswordRequest changePasswordRequest, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("something wrong with your user details"));

        System.out.println("=== Password Validation Debug ===");
        System.out.println("Current password input (raw): " + changePasswordRequest.getCurretPassword());
        System.out.println("Stored password (encoded): " + user.getPassword());
        System.out.println("New password (raw): " + changePasswordRequest.getUpdatedPassword());

        checkPasswordValidation(changePasswordRequest.getCurretPassword() , user.getPassword() , changePasswordRequest.getUpdatedPassword());

        //dirty looking for memory management purpose :(

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getUpdatedPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return UserResponse.newUserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }


    private void checkPasswordValidation( String currentPasswordInput , String currentPassword , String newPassword) {
        if(!passwordEncoder.matches(currentPasswordInput , currentPassword)){
            throw new RuntimeException("incorrect password");
        }
        if (currentPasswordInput.equals(newPassword)){
            throw new RuntimeException("this password already used in this account");
        }
    }
}
