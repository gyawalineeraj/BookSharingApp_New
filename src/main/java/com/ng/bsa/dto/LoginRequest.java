package com.ng.bsa.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "PLease provide a valid email")
    private String email;
    private String password;
}
