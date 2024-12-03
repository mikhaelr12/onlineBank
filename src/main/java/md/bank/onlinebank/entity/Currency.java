package md.bank.onlinebank.entity;

import jakarta.persistence.*;
import lombok.Data;
import md.bank.onlinebank.enums.CurrencyCode;

@Table(name = "currencies")
@Entity
@Data
public class Currency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_id_seq")
    @SequenceGenerator(name = "currency_id_seq", sequenceName = "currency_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "currency_name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyName;
}
