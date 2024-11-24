package com.ng.bsa.controller;

import com.ng.bsa.dto.ActivationRequest;
import com.ng.bsa.dto.LoginRequest;
import com.ng.bsa.dto.RegisterRequest;
import com.ng.bsa.entities.Role;
import com.ng.bsa.entities.User;
import com.ng.bsa.service.AuthService;
import com.ng.bsa.service.RoleService;
import com.ng.bsa.util.Mapper;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest)
            throws MessagingException {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        Role role = roleService.findByName("ADMIN"); //todo caching
        User user = Mapper.userMapper(registerRequest,passwordEncoder,
                List.of(role));
        System.out.println("user id in /register is " + user.getId());
        authService.registerUser(user);

        return ResponseEntity.ok("Successful");
    }
    @GetMapping ("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest){
    return authService.loginUser(loginRequest);
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestBody ActivationRequest activationRequest){
        return authService.activateAccount(activationRequest);
    }

}
