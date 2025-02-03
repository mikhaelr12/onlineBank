package md.bank.onlinebank.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder
public class AccountDTO {
    private String accountNumber;
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();
    private BigDecimal balance;
    private Long currencyId;
}
