package md.bank.onlinebank.repository;

import md.bank.onlinebank.entity.Account;
import md.bank.onlinebank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByReceiverId(Long id);


    List<Transaction> findBySenderId(Long id);
}
