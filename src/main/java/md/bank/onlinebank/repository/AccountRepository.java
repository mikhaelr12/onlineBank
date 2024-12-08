package md.bank.onlinebank.repository;

import md.bank.onlinebank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUserId(Long id);

    boolean existsByAccountNumber(String accountNumber);
}
