package com.ng.bsa.repository;

import com.ng.bsa.entities.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRespository extends JpaRepository<Book, Integer> {

    @Query("""
            SELECT b FROM Book b
            WHERE b.isbn= :isbn
            AND b.owner.id = :userID
            """)
    Optional<Book> findByIsbnAndUser(String isbn, int userID);

    @Query("""
            SELECT b FROM Book b
            WHERE b.owner.id = :userId
            """)
    Page<Book> findOwnerBooks(int userId, Pageable pageable);

    @Query("""
            SELECT b FROM Book b
            WHERE b.owner.id != :userId
            AND b.shareable = true
            AND b.borrowedStatus = false
            """)
    Page<Book> findDisplayableBooks(int userId,Pageable pageable);


    @Modifying
    @Transactional
    @Query("""
            UPDATE Book b SET b.borrowedStatus = true
            WHERE b.isbn = :isbn
            AND b.owner.id = :bookOwnerId
            """)
    void updateBorrowedStatus(int bookOwnerId,String isbn);

}
