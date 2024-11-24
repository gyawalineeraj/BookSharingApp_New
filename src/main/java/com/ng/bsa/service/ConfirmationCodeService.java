package com.ng.bsa.service;

import com.ng.bsa.entities.ConfirmationCode;
import com.ng.bsa.entities.User;
import com.ng.bsa.repository.ConfirmationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationCodeService {

    private final ConfirmationCodeRepository repository;

    public String generateAndSaveConfirmationCode(User user){
        String code = generateConfirmationCode(6);
        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .confirmationCode(code)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .createdAt(LocalDateTime.now())
                .validatedAt(null)
                .user(user)
                .build();
        repository.save(confirmationCode);
        return code;
    }

    private String generateConfirmationCode(int noOfCode) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0; i <  noOfCode; i++ ) {
                codeBuilder.append(secureRandom.nextInt(characters.length()));
        }
        return codeBuilder.toString();
    }
}
