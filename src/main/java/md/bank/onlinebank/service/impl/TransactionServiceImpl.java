package md.bank.onlinebank.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.TransactionDTO;
import md.bank.onlinebank.entity.Account;
import md.bank.onlinebank.entity.Currency;
import md.bank.onlinebank.entity.Transaction;
import md.bank.onlinebank.entity.User;
import md.bank.onlinebank.exception.AccountException;
import md.bank.onlinebank.exception.TransactionException;
import md.bank.onlinebank.repository.AccountRepository;
import md.bank.onlinebank.repository.TransactionRepository;
import md.bank.onlinebank.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserExtractServiceImpl userExtractServiceImpl;
    private final CurrencyConverterServiceImpl currencyConverterService;

    @Override
    public void makeTransaction(String jwt, TransactionDTO transactionDTO) {

        //get the accounts of the sender and receiver
        Account receiverAccount = accountRepository.findById(transactionDTO.getReceiverId())
                .orElseThrow(() -> new AccountException("Account not found"));
        Account senderAccount = accountRepository.findById(transactionDTO.getSenderId())
                .orElseThrow(() -> new AccountException("Account not found"));


        //exception if the user tries to send to the same account
        if(receiverAccount.getId().equals(senderAccount.getId())) {
            throw new AccountException("You can not transfer to the same account");
        }

        //exception if the sender wants to send more money than he has
        if(senderAccount.getBalance().subtract(transactionDTO.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new TransactionException("Insufficient balance");
        }

        //get the currencies of both accounts
        Currency senderCurrency = senderAccount.getCurrency();
        Currency receiverCurrency = receiverAccount.getCurrency();

        //subtract from the balance
        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getAmount()));

        //if the currencies are different convert
        if(!senderCurrency.equals(receiverCurrency)) {
            BigDecimal convertedAmount = currencyConverterService.convertBalance(
                    transactionDTO.getAmount(),
                    senderCurrency.getCurrencyName(),
                    receiverCurrency.getCurrencyName()
            );
            transactionDTO.setAmount(convertedAmount);
        }

        //send the money to the receiver and save changes
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDTO.getAmount()));
        accountRepository.save(receiverAccount);
        accountRepository.save(senderAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionTime(LocalTime.now());
        transaction.setSender(senderAccount);
        transaction.setReceiver(receiverAccount);

        transactionRepository.save(transaction);
    }
}
