package md.bank.onlinebank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "transactions")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "FK_SENDER_TRANSACTION"))
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", foreignKey = @ForeignKey(name = "FK_RECEIVER_TRANSACTION"))
    private Account receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_currency", foreignKey = @ForeignKey(name = "FK_SENDER_CURRENCY_TRANSACTION"))
    private Currency senderCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_currency", foreignKey = @ForeignKey(name = "FK_RECEIVER_CURRENCY-TRANSACTION"))
    private Currency receiverCurrency;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "transaction_time")
    private LocalTime transactionTime;
}
