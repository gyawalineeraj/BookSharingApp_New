package com.ng.bsa.filters;


import com.ng.bsa.entities.User;
import com.ng.bsa.repository.UserRepository;
import com.ng.bsa.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("AUTHORIZATION");
        final String email;

        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String extractedToken = token.substring(7);
        email = jwtService.extractEmail(extractedToken);

        if (email == null || !jwtService.isEmailValid(email) ||
                !jwtService.isTokenValid(extractedToken)) {
            filterChain.doFilter(request, response);
            return;
        }
//        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        User user =
                userRepository.findByEmail(email).orElseThrow(() ->
                        new UsernameNotFoundException(
                                "NO user exist in the database"));
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user, null,
                user.getRoles().stream()
                        .map((r) -> new SimpleGrantedAuthority(r.getName()))
                        .toList());
        SecurityContextHolder.getContext()
                .setAuthentication(authenticationToken);


        filterChain.doFilter(request, response);
    }
}
