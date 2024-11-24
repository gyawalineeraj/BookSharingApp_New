package com.ng.bsa.controller;

import com.ng.bsa.dto.SaveBookDto;
import com.ng.bsa.entities.Book;
import com.ng.bsa.entities.BookTransactionHistory;
import com.ng.bsa.entities.User;
import com.ng.bsa.exception.BookAlreadyRegistered;
import com.ng.bsa.response.BookPaginationResponse;
import com.ng.bsa.response.CommonResponse;
import com.ng.bsa.service.BookService;
import com.ng.bsa.service.BookTransactionHistoryService;
import com.ng.bsa.service.GetUserFromAuthenticationTokenSevice;
import com.ng.bsa.util.Mapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
@Tag(name = "BookServices")
public class BookController {

    private final BookService bookService;
    private final GetUserFromAuthenticationTokenSevice gService;
    private final BookTransactionHistoryService transactionHistoryService;


    @PostMapping
    public CommonResponse saveBook(@RequestBody SaveBookDto saveBookDto)
            throws BookAlreadyRegistered {
        User user = gService.getUserEntity();
        Book book = Mapper.toBookMapper(saveBookDto, user);
        bookService.saveBook(book);
        return CommonResponse.builder().message("Book is Saved Successfully")
                .object(saveBookDto).build();
    }

    @GetMapping("/get-owner-books")
    public BookPaginationResponse getOwnerBooks(
            @RequestParam(defaultValue = "2", required = false) int size,
            @RequestParam(required = true) int pageNumber) {
        return bookService.getOwnerBooks(pageNumber, size);
    }

    @GetMapping("/get-displayable-books")
    public BookPaginationResponse getDisplayableBooks(
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = true) int pageNumber) {
        return bookService.getDisplayableBooks(pageNumber, size);
    }


    @GetMapping("/borrow-book")
    public CommonResponse borrowBook(@RequestParam String isbn,
                                     @RequestParam int bookOwnerId) {

        var book = bookService.getBookFromIsbnAndId(isbn, bookOwnerId);
        var user = gService.getUserEntity();
        var th = new BookTransactionHistory(user, book, false, false);
        transactionHistoryService.saveTransaction(th);
        bookService.updateBorrowedStatus(isbn, bookOwnerId);
        return new CommonResponse("Book Borrowed Successfull", null);
    }

    @PutMapping("/return-book")
    public CommonResponse returnBook(@RequestParam String isbn,
                                     @RequestParam int bookOwnerId) {
        var book = bookService.getBookFromIsbnAndId(isbn, bookOwnerId);
        var user = gService.getUserEntity();
        bookService.returnBook(book, user);
        return CommonResponse.builder()
                .message("Book returned Sucessfully")
                .build();
    }
     @PutMapping("/return-approved")
    public CommonResponse approveReturnStatus(@RequestParam String isbn,
                                     @RequestParam int bookOwnerId) {
        var book = bookService.getBookFromIsbnAndId(isbn, bookOwnerId);
        var user = gService.getUserEntity();
        bookService.approveReturn(book, user);
        return CommonResponse.builder()
                .message("Book returned Approved Successfully")
                .build();
    }



}
