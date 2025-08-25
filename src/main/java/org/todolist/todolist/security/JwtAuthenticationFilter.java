package org.todolist.todolist.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.todolist.todolist.entity.User;
import org.todolist.todolist.service.UserService;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }


        System.out.println("=== JWT Filter Debug ===");
        System.out.println("Request URL: " + request.getRequestURL());

        String token = getTokenFromRequest(request);

        System.out.println("Token extracted: " + (token != null ? "YES" : "NO"));

        if(token != null && jwtTokenProvider.validateToken(token)){
            String userEmail = jwtTokenProvider.getUserEmail(token);
            System.out.println("User email from token: " + userEmail);


            User currentUser = userService.getUserByEmail(userEmail);

            if(currentUser != null){
                System.out.println("Token is valid");

                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                System.out.println("User email from token: " + userEmail);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails , null , userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request , response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }
}
