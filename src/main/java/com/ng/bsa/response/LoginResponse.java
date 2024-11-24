package com.ng.bsa.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private String JwtToken;
}
