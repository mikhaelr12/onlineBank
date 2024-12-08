package md.bank.onlinebank.exception;

import java.io.Serial;

public class CurrencyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public CurrencyException(String message) {
        super(message);
    }
}
