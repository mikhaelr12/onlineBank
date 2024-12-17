package md.bank.onlinebank.service;

import md.bank.onlinebank.dto.TransactionDTO;
import md.bank.onlinebank.dto.request.TransactionFilterDTO;

import java.util.List;

public interface TransactionService {
    void makeTransaction(String jwt, TransactionDTO transactionDTO);

    List<TransactionDTO> getAllTransactions(String jwt, TransactionFilterDTO transactionFilterDTO);
}
