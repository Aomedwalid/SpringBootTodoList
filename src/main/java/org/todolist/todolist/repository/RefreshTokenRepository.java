package org.todolist.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.todolist.todolist.entity.RefreshToken;
import org.todolist.todolist.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken , Long> {
    Optional<RefreshToken> findByToken(String token);


    List<RefreshToken> findByUser(User user);

    @Modifying
    @Query("delete from RefreshToken r where r.user = :user and r.revoked = true")
    void deleteUserExpiredTokens(User user);

    @Modifying
    @Query("update RefreshToken r set r.revoked = true where r.user = :user")
    void revokeAllUserTokens(User user);

    void deleteAllByUserEmail(String userEmail);

    void deleteByToken(String token);
}
