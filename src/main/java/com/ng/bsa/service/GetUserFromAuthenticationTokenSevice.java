package com.ng.bsa.service;

import com.ng.bsa.entities.User;
import com.ng.bsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserFromAuthenticationTokenSevice {




    private final UserRepository userRepository;

    public String getUserEmail() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        var a = (UserDetails) authentication.getPrincipal();
        return a.getUsername();
    }

    public User getUserEntity() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        var user = (User) authentication.getPrincipal();
        return user;
    }
}
