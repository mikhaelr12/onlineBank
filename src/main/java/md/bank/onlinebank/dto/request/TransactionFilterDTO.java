package md.bank.onlinebank.dto.request;

import lombok.Data;
import md.bank.onlinebank.enums.SortDirection;

import java.time.LocalDate;

@Data
public class TransactionFilterDTO {
    private LocalDate transactionDate;
    private SortDirection sortByAmount;
}

