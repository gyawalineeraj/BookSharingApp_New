package com.ng.bsa.service;

import com.ng.bsa.entities.Book;
import com.ng.bsa.entities.BookTransactionHistory;
import com.ng.bsa.entities.User;
import com.ng.bsa.exception.BookAlreadyRegistered;
import com.ng.bsa.exception.InvalidPageNumber;
import com.ng.bsa.repository.BookRespository;
import com.ng.bsa.repository.BookTransactionHistoryRepositoy;
import com.ng.bsa.response.BookPaginationResponse;
import com.ng.bsa.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRespository bookRespository;
    private final GetUserFromAuthenticationTokenSevice gService;
    private final BookTransactionHistoryRepositoy bookTransactionHistoryRepositoy;

    public void saveBook(Book book) throws BookAlreadyRegistered {

        var dbBook = bookRespository.findByIsbnAndUser(book.getIsbn(),
                gService.getUserEntity().getId());
        if (dbBook.isPresent()) {
            throw new BookAlreadyRegistered("Book with the Isbn already " +
                    "exist for the user");
        }
        bookRespository.save(book);
    }

    public BookPaginationResponse getOwnerBooks(int pageNumber, int size) {

        Pageable pageable = getPageable(pageNumber, size);
        var userId = gService.getUserEntity().getId();
        Page<Book> bookPage = bookRespository.findOwnerBooks(userId, pageable);
        return Mapper.toBookPaginationResponse(bookPage);
    }

    public BookPaginationResponse getDisplayableBooks(int pageNumber,
                                                      int size) {
        Pageable pageable = getPageable(pageNumber, size);
        var userId = gService.getUserEntity().getId();
        Page<Book> bookPage = bookRespository.findDisplayableBooks(userId,
                pageable);
        return Mapper.toBookPaginationResponse(bookPage);

    }

    private Pageable getPageable(int pageNumber, int size) {
        if (pageNumber <= 0) {
            throw new InvalidPageNumber("Your page number is invalid  for the" +
                    " pagination ");
        }

        return PageRequest.of((pageNumber - 1), size);
    }


    public Book getBookFromIsbnAndId(String isbn, int id) {
        return bookRespository.findByIsbnAndUser(isbn, id)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    public void updateBorrowedStatus(String isbn, int id) {
        bookRespository.updateBorrowedStatus(id, isbn);
    }


    public void returnBook(Book book, User user) {
        BookTransactionHistory history =
                bookTransactionHistoryRepositoy.findByUser(user)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid arguments"));

        history.setReturned(true);
        book.setBorrowedStatus(false);
        bookTransactionHistoryRepositoy.save(history);
        bookRespository.save(book);
    }

    public void approveReturn(Book book, User user) {
        BookTransactionHistory history =
                bookTransactionHistoryRepositoy.findByUser(user)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid arguments"));
            history.setReturnedApproved(true);
            bookTransactionHistoryRepositoy.save(history);
    }
}
