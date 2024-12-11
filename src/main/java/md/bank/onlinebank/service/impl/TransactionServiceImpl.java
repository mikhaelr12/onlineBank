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
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserExtractServiceImpl userExtractServiceImpl;
    private final CurrencyConverterServiceImpl currencyConverterService;

    @Override
    @Transactional
    public void makeTransaction(String jwt, TransactionDTO transactionDTO) {

        User user = userExtractServiceImpl.getUser(jwt);

        //get the accounts of the sender and receiver
        Account receiverAccount = accountRepository.findByAccountNumber(transactionDTO.getReceiverNumber());
        if (receiverAccount == null) {
            throw new AccountException("No account found with number" + transactionDTO.getReceiverNumber());
        }

        Account senderAccount = accountRepository.findByUserId(user.getId());

        //exception if the amount is negative or 0
        if(transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new TransactionException("Amount must be greater than zero,"
                    + transactionDTO.getAmount() + " is an invalid amount");

        //exception if the user tries to send to the same account
        if(receiverAccount.getId().equals(senderAccount.getId()))
            throw new AccountException("You can not transfer to the same account, source:"
                    + senderAccount.getUser().getUsername() + ", destination:" + receiverAccount.getId());


        //exception if the sender wants to send more money than he has
        if(senderAccount.getBalance().subtract(transactionDTO.getAmount()).compareTo(BigDecimal.ZERO) < 0) 
            throw new TransactionException("Insufficient balance" + senderAccount.getBalance());


        //get the currencies of both accounts
        Currency senderCurrency = senderAccount.getCurrency();
        Currency receiverCurrency = receiverAccount.getCurrency();

        BigDecimal finalAmount = transactionDTO.getAmount();

        //if the currencies are different convert
        if(!senderCurrency.equals(receiverCurrency)) {
            finalAmount = currencyConverterService.convertBalance(
                    transactionDTO.getAmount(),
                    senderCurrency.getCurrencyName(),
                    receiverCurrency.getCurrencyName()
            );
        }

        //subtract from the balance
        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getAmount()));

        //send the money to the receiver and save changes
        receiverAccount.setBalance(receiverAccount.getBalance().add(finalAmount));

        accountRepository.saveAll(List.of(receiverAccount, senderAccount));

        transactionRepository.save(Transaction.builder()
                        .amount(finalAmount)
                        .transactionDate(LocalDate.now())
                        .transactionTime(LocalTime.now())
                        .receiver(receiverAccount)
                        .sender(senderAccount)
                        .senderCurrency(senderCurrency)
                        .receiverCurrency(receiverCurrency)
                .build());
    }

    @Override
    public List<TransactionDTO> getAllTransactions(String jwt) {
        User user = userExtractServiceImpl.getUser(jwt);
        Account account = accountRepository.findByUserId(user.getId());
        List<Transaction> sender = transactionRepository.findBySenderId(account.getId());
        List<Transaction> receiver = transactionRepository.findByReceiverId(account.getId());
        List<Transaction> mergedLists = Stream.concat(
                sender.stream(),
                receiver.stream()
        ).toList();
        return mergedLists.stream().map(t -> TransactionDTO.builder()
                .amount(t.getAmount())
                .transactionDate(t.getTransactionDate())
                .transactionTime(t.getTransactionTime())
                .senderId(t.getSender().getId())
                .receiverNumber(t.getReceiver().getAccountNumber())
                .senderCurrency(t.getSenderCurrency().getId())
                .receiverCurrency(t.getReceiverCurrency().getId())
                .build()
        ).toList();
    }
}
