package org.todolist.todolist.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JwtConfig {
    @Value("${jwt.secret}")
    private String jwtKey;

    @Value("${jwt.access-expiration}")
    private Long jwtAccessExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long jwtRefreshExpiration;
}