package md.bank.onlinebank.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data @Builder
public class TransactionDTO {
    private Long id;
    private Long senderId;
    private String receiverNumber;
    private Long senderCurrency;
    private Long receiverCurrency;
    private BigDecimal amount;
    private LocalTime transactionTime;
    private LocalDate transactionDate;
}
