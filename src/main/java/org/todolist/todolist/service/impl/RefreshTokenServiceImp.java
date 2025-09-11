package org.todolist.todolist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.todolist.todolist.entity.RefreshToken;
import org.todolist.todolist.entity.User;
import org.todolist.todolist.exceptions.customerrors.InvalidTokenException;
import org.todolist.todolist.repository.RefreshTokenRepository;
import org.todolist.todolist.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImp implements RefreshTokenService {
    private final RefreshTokenRepository refreshRepo;

    @Override
    public RefreshToken createRefreshToken(User user) {
        //clean the database from this user refresh tokens
        refreshRepo.deleteAllByUserEmail(user.getEmail());
        //delete query should fires first
        refreshRepo.flush();

        RefreshToken result = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .revoked(false)
                .user(user)
                .build();

        return refreshRepo.save(result);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshRepo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Cannot find the refresh token"));
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        validateToken(refreshToken);

        return refreshRepo.save(refreshToken);
    }

    @Override
    public void revokeToken(RefreshToken refreshToken) {
        validateToken(refreshToken);

        refreshToken.setRevoked(true);
        refreshRepo.save(refreshToken);
    }

    @Override
    public void revokeAllUserTokens(RefreshToken refreshToken) {
        refreshRepo.revokeAllUserTokens(refreshToken.getUser());
    }

    @Override
    public void deleteAllUserTokens(String email) {
        refreshRepo.deleteAllByUserEmail(email);
    }



    @Override
    public void deleteExpiredTokens(RefreshToken refreshToken) {
        refreshRepo.deleteUserExpiredTokens(refreshToken.getUser());
    }

    @Override
    public boolean validTokenBoolean(RefreshToken refreshToken) {
        return refreshToken.isValid();
    }


    private void validateToken(RefreshToken refreshToken){
        if (!refreshToken.isValid()){
            refreshRepo.deleteByToken(refreshToken.getToken());
            throw new RuntimeException("Refresh token is invalid");
        }
    }
}
