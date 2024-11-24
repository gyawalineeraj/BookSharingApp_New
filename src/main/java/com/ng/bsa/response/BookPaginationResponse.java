package com.ng.bsa.response;

import com.ng.bsa.dto.ReturnBookDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookPaginationResponse {

    private List<ReturnBookDto> bookDtoList;
    private long totalElements;
    private int totalPages;
    private int currentPageNumber;
    private int size;
}
