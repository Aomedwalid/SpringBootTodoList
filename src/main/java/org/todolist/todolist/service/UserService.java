package org.todolist.todolist.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.todolist.todolist.dto.request.ChangePasswordRequest;
import org.todolist.todolist.dto.request.UpdateUserNameRequest;
import org.todolist.todolist.dto.response.UserResponse;
import org.todolist.todolist.entity.User;

public interface UserService {
    UserResponse changeUserPassword(ChangePasswordRequest changePasswordRequest, UserDetails user);

    User getUserByEmail(String email);
    UserResponse getUserResponse(String email);

    UserResponse updateUser(UpdateUserNameRequest updateDetails, UserDetails user);

    void deleteUserAccount(String email);
}
