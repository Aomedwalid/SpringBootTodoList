package org.todolist.todolist.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.todolist.todolist.dto.request.LoginRequest;
import org.todolist.todolist.dto.request.RegisterRequest;
import org.todolist.todolist.dto.response.AuthResponse;
import org.todolist.todolist.entity.User;
import org.todolist.todolist.security.CustomUserDetails;

public interface AuthService {

    //register the user

    AuthResponse register(RegisterRequest request , HttpServletResponse response);

    //login

    AuthResponse login(LoginRequest loginRequest , HttpServletResponse response);

    //user fetching

    void logout(String refreshToken , HttpServletResponse response);

    User getUserFromToken(String Token);

    Boolean existedByEmail(String email);

    AuthResponse refreshAccessToken(String token);
}
