package org.todolist.todolist.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.todolist.todolist.dto.request.LoginRequest;
import org.todolist.todolist.dto.request.RegisterRequest;
import org.todolist.todolist.dto.response.AuthResponse;
import org.todolist.todolist.dto.response.UserResponse;
import org.todolist.todolist.security.CustomUserDetails;
import org.todolist.todolist.service.AuthService;
import org.todolist.todolist.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    public final AuthService authService;
    public final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createAccount(@Valid @RequestBody RegisterRequest request , HttpServletResponse response){
        AuthResponse authResponse = authService.register(request , response);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request , HttpServletResponse response){
        AuthResponse authResponse = authService.login(request , response);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("logout")
    public ResponseEntity<String> logoutUser(@AuthenticationPrincipal CustomUserDetails user
            ,@CookieValue(value = "refresh_token" , required = false) String refreshToken
            ,HttpServletResponse response){
        authService.logout(user.getUsername() ,refreshToken ,  response);
        return ResponseEntity.ok("User is no longer authenticated");
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkAuth() {
        return ResponseEntity.ok("Authentication service is working");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshAccessToken(@CookieValue(value = "refresh_token" , required = false) String refreshToken) {
        System.out.println(refreshToken);
        if (refreshToken == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.failedResponse("fail request"));
        }

        AuthResponse RefreshResponse = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(RefreshResponse);
    }

    @GetMapping("/me")//in this endpoint is scalable so....
    //if u need to put a nondetailed informaiton about the user with this endpoint u can .... as u like ;D
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal CustomUserDetails user){
        UserResponse response = userService.getUserResponse(user.getUsername());
        return ResponseEntity.ok(response);
    }
}
