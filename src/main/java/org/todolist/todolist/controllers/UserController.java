package org.todolist.todolist.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.todolist.todolist.dto.request.ChangePasswordRequest;
import org.todolist.todolist.dto.request.UpdateUserNameRequest;
import org.todolist.todolist.dto.response.UserResponse;
import org.todolist.todolist.entity.User;
import org.todolist.todolist.service.UserService;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(@AuthenticationPrincipal UserDetails userDetails){
        UserResponse userResponse = userService.getUserResponse(userDetails.getUsername());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateUserProfile(@RequestBody UpdateUserNameRequest updatedDetails,
                                                          @AuthenticationPrincipal UserDetails userDetails){
        UserResponse response = userService.updateUser(updatedDetails , userDetails);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<UserResponse> updateUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                           @AuthenticationPrincipal UserDetails userDetails){
        UserResponse response = userService.changeUserPassword(changePasswordRequest , userDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/account")
    public void deleteUserAccount(@AuthenticationPrincipal UserDetails userDetails){
        userService.deleteUserAccount(userDetails.getUsername());
    }
}
