package com.ng.bsa.service;

import com.ng.bsa.entities.BookTransactionHistory;
import com.ng.bsa.repository.BookTransactionHistoryRepositoy;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class BookTransactionHistoryService {

    private final BookTransactionHistoryRepositoy transactionRepositoy;

    public void saveTransaction(BookTransactionHistory t){
       transactionRepositoy.save(t);
    }
}
