package org.todolist.todolist.service;

import org.todolist.todolist.entity.RefreshToken;
import org.todolist.todolist.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken refreshToken);

    void revokeToken(RefreshToken refreshToken);
    void revokeAllUserTokens(RefreshToken refreshToken);
    void deleteAllUserTokens(String email);
    void deleteExpiredTokens(RefreshToken refreshToken);

    boolean validTokenBoolean(RefreshToken refreshToken);
}
