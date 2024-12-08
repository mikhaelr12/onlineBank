package md.bank.onlinebank.service.impl;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;

@Service
public class CurrencyConverterServiceImpl {

    public  BigDecimal convertBalance(BigDecimal balance, String currentCurrency, String targetCurrency) {
        MonetaryAmount currentMoney = Money.of(balance, currentCurrency);

        MonetaryAmount convertedAmount = convertCurrency(currentMoney, targetCurrency);

        return convertedAmount.getNumber().numberValue(BigDecimal.class);
    }

    private  MonetaryAmount convertCurrency(MonetaryAmount amount, String targetCurrency) {
        return MonetaryConversions.getConversion(targetCurrency).apply(amount);
    }
}
