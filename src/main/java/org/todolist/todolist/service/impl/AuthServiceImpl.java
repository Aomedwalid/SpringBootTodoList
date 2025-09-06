package org.todolist.todolist.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.todolist.todolist.dto.request.LoginRequest;
import org.todolist.todolist.dto.request.RegisterRequest;
import org.todolist.todolist.dto.response.AuthResponse;
import org.todolist.todolist.entity.RefreshToken;
import org.todolist.todolist.entity.User;
import org.todolist.todolist.exceptions.customerrors.InvalidTokenException;
import org.todolist.todolist.exceptions.customerrors.UserAlreadyExistsException;
import org.todolist.todolist.repository.UserRepository;
import org.todolist.todolist.security.JwtTokenProvider;
import org.todolist.todolist.service.AuthService;
import org.todolist.todolist.service.RefreshTokenService;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse register(RegisterRequest request , HttpServletResponse response) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        if(existedByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("this email already existed");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);
        String accessToken = jwtTokenProvider.generateAccessToken(savedUser.getEmail());

        saveRefreshTokenInsideCookies(refreshToken.getToken() , response);

        return AuthResponse.success(
                accessToken,
                jwtTokenProvider.getAccessTokenExpiration(),
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                "Registration success"
        );
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest , HttpServletResponse response) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("can't identify your identity"));

        String accessToken = jwtTokenProvider.generateAccessToken(loginRequest.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        saveRefreshTokenInsideCookies(refreshToken.getToken() , response);

        return AuthResponse.success(
                accessToken,
                jwtTokenProvider.getAccessTokenExpiration(),
                user.getId(),
                user.getEmail(),
                user.getName(),
                "Login success"
        );
    }

    @Override
    public void logout(String email , String refreshToken , HttpServletResponse response) {
        log.info("Logout request for user");

        removeRefreshTokenFromCookies( response);
        refreshTokenService.deleteAllUserTokens(email);

        SecurityContextHolder.clearContext();

        log.info("User logged out successfully");
    }

    @Override
    public User getUserFromToken(String token) {
        String email = jwtTokenProvider.getUserEmail(token);

        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("can't identify your identity"));
    }

    @Override
    public Boolean existedByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        RefreshToken currentRefreshToken = refreshTokenService.findByToken(refreshToken);

        if(!refreshTokenService.validTokenBoolean(currentRefreshToken)){
            throw new InvalidTokenException("refresh token is invalid");
        }

        User user = currentRefreshToken.getUser();

        //generate new token
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());

        return AuthResponse.success(
                accessToken,
                jwtTokenProvider.getAccessTokenExpiration(),
                user.getId(),
                user.getEmail(),
                user.getName(),
                "the token hass been refreshed"
        );

    }

    private void saveRefreshTokenInsideCookies(String refreshToken , HttpServletResponse response){
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")   // ✅ SameSite supported here
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    private void removeRefreshTokenFromCookies(HttpServletResponse response){
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)   // 👈 tells browser to delete it immediately
                .build();

        response.addHeader("Set-Cookie", deleteCookie.toString());
    }
}
