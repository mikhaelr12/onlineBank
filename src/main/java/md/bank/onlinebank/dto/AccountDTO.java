package md.bank.onlinebank.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data @Builder
public class AccountDTO {
    private String accountNumber;
    private LocalDate createdAt;
    private BigDecimal balance;
    private Long currency;
}
