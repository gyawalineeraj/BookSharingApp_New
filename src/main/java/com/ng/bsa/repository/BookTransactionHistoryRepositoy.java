package com.ng.bsa.repository;

import com.ng.bsa.entities.BookTransactionHistory;
import com.ng.bsa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookTransactionHistoryRepositoy extends JpaRepository<BookTransactionHistory,Integer> {

    Optional<BookTransactionHistory> findByUser(User user);
}
