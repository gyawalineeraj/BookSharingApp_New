package com.ng.bsa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "firstName is required")
    @NotBlank(message = "firstName cannot be blank")
    private String firstname;
    @NotNull(message = "lastName is required")
    @NotBlank(message = "LastName cannot be blank")
    private String lastname;
    @NotNull(message = "email is required")
    @NotBlank(message = "email cannot be blank")
    @Email(message = "Email is not on proper Format")
    private String email;
    @NotNull(message = "password is required")
    @NotBlank(message = "password cannot be blank")
    @Size(min = 3, message = "password must be at least 4 characters")
    private String password;
}
