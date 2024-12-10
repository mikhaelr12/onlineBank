package md.bank.onlinebank.service;

import md.bank.onlinebank.dto.TransactionDTO;

public interface TransactionService {
    void makeTransaction(String jwt, TransactionDTO transactionDTO);
}
