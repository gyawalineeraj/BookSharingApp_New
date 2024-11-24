package com.ng.bsa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationCode {

    @Id
    @GeneratedValue
    private Integer id;

    private String confirmationCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
