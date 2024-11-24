package com.ng.bsa.util;

import com.ng.bsa.dto.RegisterRequest;
import com.ng.bsa.dto.ReturnBookDto;
import com.ng.bsa.dto.SaveBookDto;
import com.ng.bsa.entities.Book;
import com.ng.bsa.entities.Role;
import com.ng.bsa.entities.User;
import com.ng.bsa.response.BookPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class Mapper {

    public static User userMapper(RegisterRequest registerRequest,
                                  PasswordEncoder passwordEncoder,
                                  List<Role> roleList) {
        return User
                .builder()
                .firstName(registerRequest.getFirstname())
                .lastName(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(
                        registerRequest.getPassword()))
                .accountLocked(false)
                .roles(roleList)
                .enabled(false)
                .build();
    }


    public static Book toBookMapper(SaveBookDto saveBookDto, User user) {
        return Book.builder()
                .owner(user)
                .bookCover(saveBookDto.getBookCover())
                .isbn(saveBookDto.getIsbn())
                .archived(false)
                .shareable(saveBookDto.isShareable())
                .authorName(saveBookDto.getAuthorName())
                .synopsis(saveBookDto.getSynopsis())
                .title(saveBookDto.getTitle())
                .build();
    }

    public static ReturnBookDto toReturnBookDto(Book book) {
        return ReturnBookDto.builder()
                .title(book.getTitle())
                .synopsis(book.getSynopsis())
                .bookCover(book.getBookCover())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .ownerId(book.getOwner().getId())
                .build();
    }


    public static BookPaginationResponse toBookPaginationResponse(
            Page<Book> page) {
        return BookPaginationResponse.builder()
                .size(page.getSize())
                .currentPageNumber(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .bookDtoList(page.getContent().stream()
                        .map((b) -> Mapper.toReturnBookDto(b)
                        ).toList())

                .build();
    }
}
