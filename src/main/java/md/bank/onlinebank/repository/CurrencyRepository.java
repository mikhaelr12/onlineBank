package md.bank.onlinebank.repository;

import md.bank.onlinebank.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
