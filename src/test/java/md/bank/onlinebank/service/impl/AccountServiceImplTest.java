package md.bank.onlinebank.service.impl;

import md.bank.onlinebank.dto.AccountDTO;
import md.bank.onlinebank.entity.Account;
import md.bank.onlinebank.entity.Currency;
import md.bank.onlinebank.entity.User;
import md.bank.onlinebank.exception.AccountException;
import md.bank.onlinebank.repository.AccountRepository;
import md.bank.onlinebank.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    @Mock
    private UserExtractServiceImpl userExtractServiceImpl;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private  AccountServiceImpl accountServiceImpl;

    private String jwtToken;
    private User testUser;
    private User testUser2;
    private Currency testCurrency;
    private AccountDTO testAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtToken = "valid-jwt";
        testUser = new User();
        testUser2 = new User();
        testCurrency = new Currency();
        testCurrency.setId(1L);
        testUser.setUsername("testUser");
        testUser.setId(1L);
        testUser2.setId(2L);
    }

    @Test
    void accountAlreadyExists() {
        when(userExtractServiceImpl.getUser(jwtToken)).thenReturn(testUser);
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setUser(testUser);
        when(accountRepository.findByUserId(testUser.getId())).thenReturn(existingAccount);
        assertThrows(AccountException.class, () -> accountServiceImpl.createAccount(jwtToken, testAccount));
    }

    @Test
    void createNewAccount(){
        when(userExtractServiceImpl.getUser(jwtToken)).thenReturn(testUser);
        when(accountRepository.findByUserId(testUser.getId())).thenReturn(null);
        Account newAccount = new Account();
        newAccount.setId(1L);
        newAccount.setUser(testUser);
        newAccount.setAccountNumber("412412412412412");
        newAccount.setCurrency(testCurrency);
        newAccount.setCreatedAt(LocalDate.now());

        when(accountRepository.findByUserId(testUser.getId())).thenReturn(newAccount);
    }
}