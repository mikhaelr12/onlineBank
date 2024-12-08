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
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserExtractServiceImpl userExtract;
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyConverterServiceImpl currencyConverter;

    private String generateAccountNumber() {
        String prefix = "4771";
        String accountNumber;

        do {
            int randomPart = new Random().nextInt(90000) + 10000;
            accountNumber = prefix + randomPart;
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }


    @Override
    public List<AccountDTO> getAllAcounts(String token) {
        User user = userExtract.getUser(token);

        List<Account> accounts = accountRepository.findAllByUserId(user.getId());
        if (accounts.isEmpty()) {
            throw new AccountException("No account found");
        }
        return accounts.stream().map(a -> AccountDTO.builder()
                .accountNumber(a.getAccountNumber())
                .balance(a.getBalance())
                .createdAt(a.getCreatedAt())
                .currency(a.getCurrency().getId())
                .build()
        ).toList();
    }

    @Override
    public void createAccount(String token, AccountDTO accountDTO) {
        User user = userExtract.getUser(token);
        List<Account> accounts = accountRepository.findAllByUserId(user.getId());

        if(accounts.size() == 10){
            throw new AccountException("Cant have more than 10 accounts");
        }

        Optional<Currency> currency = currencyRepository.findById(accountDTO.getCurrency());

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDate.now());
        account.setCurrency(currency.get());
        account.setUser(user);
        accountRepository.save(account);
    }

    @Override
    public void depositAccount(String token, Long id, AccountDTO accountDTO) {
        User user = userExtract.getUser(token);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccountException("You don't have permission to deposit into this account");
        }

        Currency depositCurrency = currencyRepository.findById(accountDTO.getCurrency())
                .orElseThrow(() -> new AccountException("Currency not found"));


        BigDecimal convertedValue = currencyConverter.convertBalance(
                accountDTO.getBalance(),
                depositCurrency.getCurrencyName(),
                account.getCurrency().getCurrencyName()
        );

        account.setBalance(account.getBalance().add(convertedValue));

        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void changeCurrency(String jwt, Long id, AccountDTO accountDTO) {
        User user = userExtract.getUser(jwt);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account not found for ID: " + id));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccountException("You don't have permission to change the currency for account ID: " + id);
        }

        Currency newCurrency = currencyRepository.findById(accountDTO.getCurrency())
                .orElseThrow(() -> new AccountException("Currency not found for ID: " + accountDTO.getCurrency()));

        if (newCurrency.getId().equals(account.getCurrency().getId())) {
            throw new CurrencyException("Account ID " + id + " is already in the specified currency: " + newCurrency.getCurrencyName());
        }

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
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account not found for ID: " + id));
        if(!account.getUser().getId().equals(user.getId())){
            throw new AccountException("You don't have permission to delete account ID: " + id);
        }
        if(account.getBalance().compareTo(BigDecimal.ZERO) > 0){
            throw new AccountException("You can not delete your account because you have money on it," +
                    " please empty it");
        }
        accountRepository.delete(account);
    }

}
