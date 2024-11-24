package com.ng.bsa.service;

import com.ng.bsa.dto.ActivationRequest;
import com.ng.bsa.dto.LoginRequest;
import com.ng.bsa.entities.ConfirmationCode;
import com.ng.bsa.entities.User;
import com.ng.bsa.repository.ConfirmationCodeRepository;
import com.ng.bsa.repository.UserRepository;
import com.ng.bsa.response.LoginResponse;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepositor;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    @Transactional
    public void registerUser(User user) throws MessagingException {
        userRepositor.save(user);
        emailService.sendConfirmationEmail(user, "Confirm Email");
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        var user = userRepositor.findByEmail(loginRequest.getEmail()).get();
        boolean enabled = user.isEnabled();
        if (!enabled) {
            return ResponseEntity.badRequest().body("user email is not " +
                    "verified");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));
        var token = jwtService.generateJwtToken(null,
                loginRequest.getEmail());
        return ResponseEntity.ok(
                new LoginResponse("Login Sucessfull", token));


    }

    public ResponseEntity<?> activateAccount(
            ActivationRequest activationRequest) {
        ConfirmationCode confirmationCode =
                confirmationCodeRepository.findByUserEmail(
                        activationRequest.getEmail()).get();
        System.out.println("confirmation code in db" +
                confirmationCode.getConfirmationCode());
        System.out.println(
                "confirmation code by user" + activationRequest.toString());
        var result =
                confirmationCode.getExpiresAt().isAfter(LocalDateTime.now());

        if (result) {
            if (confirmationCode.getConfirmationCode()
                    .equals(activationRequest.getCode())) {
                confirmationCode.getUser().setEnabled(true);
                ;
                confirmationCode.setValidatedAt(LocalDateTime.now());
                confirmationCodeRepository.save(confirmationCode);
                return ResponseEntity.ok("account Activation Sucessfull");
            }

        }
        return ResponseEntity.badRequest().body("invalid Acitvation Code");
    }


}
