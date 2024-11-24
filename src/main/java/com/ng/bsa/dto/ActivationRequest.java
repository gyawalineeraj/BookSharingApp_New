package com.ng.bsa.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class ActivationRequest {

    private String code;
    private String email;
}
