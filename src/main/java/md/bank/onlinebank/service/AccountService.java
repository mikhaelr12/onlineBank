package md.bank.onlinebank.service;

import md.bank.onlinebank.dto.AccountDTO;

public interface AccountService {
    AccountDTO getAccount(String token);

    void createAccount(String token, AccountDTO accountDTO);

    void depositAccount(String jwt,AccountDTO accountDTO);

    void changeCurrency(String jwt, Long id, AccountDTO accountDTO);

    void deleteAccount(String jwt, Long id);
}
