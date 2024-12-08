package md.bank.onlinebank.service;

import md.bank.onlinebank.dto.AccountDTO;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAllAcounts(String token);

    void createAccount(String token, AccountDTO accountDTO);

    void depositAccount(String jwt, Long id, AccountDTO accountDTO);

    void changeCurrency(String jwt, Long id, AccountDTO accountDTO);

    void deleteAccount(String jwt, Long id);
}
