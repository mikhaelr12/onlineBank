package md.bank.onlinebank.repository;

import md.bank.onlinebank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByReceiverId(Long id);

    List<Transaction> findBySenderId(Long id);

    List<Transaction> findByTransactionDate(LocalDate transactionDate);
}
