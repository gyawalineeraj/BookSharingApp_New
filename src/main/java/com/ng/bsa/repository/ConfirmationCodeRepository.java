package com.ng.bsa.repository;

import com.ng.bsa.entities.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode,Integer> {
@Query("""
        SELECT c from ConfirmationCode c 
        WHERE c.user.email= :email
        """)
    Optional<ConfirmationCode> findByUserEmail(String email);
}
