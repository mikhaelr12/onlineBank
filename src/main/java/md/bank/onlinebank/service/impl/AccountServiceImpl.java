package md.bank.onlinebank.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.AccountDTO;
import md.bank.onlinebank.entity.Account;
import md.bank.onlinebank.entity.Currency;
import md.bank.onlinebank.entity.User;
import md.bank.onlinebank.exception.AccountException;
import md.bank.onlinebank.exception.CurrencyException;
import md.bank.onlinebank.repository.AccountRepository;
import md.bank.onlinebank.repository.CurrencyRepository;
import md.bank.onlinebank.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserExtractServiceImpl userExtract;
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyConverterServiceImpl currencyConverter;

    //generator for account number
    private String generateAccountNumber() {

        //all accounts will have this prefix
        String prefix = "4771";
        String accountNumber;

        //check if there is an existing account number with the one generated in this loop
        do {
            int randomPart = new Random().nextInt(90000) + 10000;
            accountNumber = prefix + randomPart;
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }


    @Override
    public AccountDTO getAccount(String token) {
        User user = userExtract.getUser(token);

        //find the account
        Account account = accountRepository.findByUserId(user.getId());

        //exception that no account found
        if (account == null) {
            throw new AccountException("Account not found");
        }

        //return the account
        return AccountDTO.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .currency(account.getCurrency().getId())
                .build();
    }

    @Override
    public void createAccount(String token, AccountDTO accountDTO) {
        User user = userExtract.getUser(token);

        //find the account
        Account account = accountRepository.findByUserId(user.getId());

        //exception that there is no account
        if(account != null) {
            throw new AccountException("Account already exists");
        }

        //get the currency for the new account
        Currency currency = currencyRepository.findById(accountDTO.getCurrency())
                .orElseThrow(() -> new CurrencyException("Currency not found"));

        //save the new created account
        accountRepository.save(Account.builder()
                        .accountNumber(generateAccountNumber())
                        .balance(BigDecimal.ZERO)
                        .createdAt(LocalDate.now())
                        .currency(currency)
                        .user(user)
                .build());
    }

    @Override
    public void depositAccount(String token, AccountDTO accountDTO) {
        User user = userExtract.getUser(token);

        //find the account of the user
        Account account = accountRepository.findByUserId(user.getId());

        //exception that there is no account
        if(account == null) {
            throw new AccountException("Account not found");
        }

        //exception if the user tries to do something on another account
        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccountException("You don't have permission to deposit into this account");
        }

        //check if the user chose a valid currency
        Currency depositCurrency = currencyRepository.findById(accountDTO.getCurrency())
                .orElseThrow(() -> new AccountException("Currency not found"));

        //get the amount
        BigDecimal depositAmount = accountDTO.getBalance();

        //if the currency chosen by the user is different from the one on his account it is converted
        if(!account.getCurrency().getId().equals(depositCurrency.getId())) {
             depositAmount = currencyConverter.convertBalance(
                    accountDTO.getBalance(),
                    depositCurrency.getCurrencyName(),
                    account.getCurrency().getCurrencyName()
            );
        }

        //add the amount to the balance and save
        account.setBalance(account.getBalance().add(depositAmount));

        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void changeCurrency(String jwt, Long id, AccountDTO accountDTO) {
        User user = userExtract.getUser(jwt);

        //get the account
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account " + id + " not found"));

        //exception if the user tries to change the currency of an account that does not belong to him
        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccountException("You don't have permission to change the currency for account: " + user.getUsername());
        }

        //search the new currency
        Currency newCurrency = currencyRepository.findById(accountDTO.getCurrency())
                .orElseThrow(() -> new AccountException("Currency not found for ID: " + accountDTO.getCurrency()));

        //exception if the user tries to change the currency to the current one
        if (newCurrency.getId().equals(account.getCurrency().getId())) {
            throw new CurrencyException("Account " + account.getAccountNumber() + " is already in the specified currency: "
                    + account.getCurrency().getCurrencyName());
        }

        //convert the currency
        BigDecimal convertedValue = currencyConverter.convertBalance(
                account.getBalance(),
                account.getCurrency().getCurrencyName(),
                newCurrency.getCurrencyName()
        );

        account.setCurrency(newCurrency);
        account.setBalance(convertedValue);

        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(String jwt, Long id) {
        User user = userExtract.getUser(jwt);

        //get the account
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account not found for ID: " + id));

        //exception if the user tries to delete someone else's account
        if(!account.getUser().getId().equals(user.getId())){
            throw new AccountException("You don't have permission to delete account ID: " + id);
        }

        //exception if the user wants to delete his account, but he has money on it
        if(account.getBalance().compareTo(BigDecimal.ZERO) > 0){
            throw new AccountException("You can not delete your account because you have money on it," +
                    " please empty it");
        }
        accountRepository.delete(account);
    }
}
