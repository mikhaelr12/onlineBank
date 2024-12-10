package md.bank.onlinebank.exception;

import java.io.Serial;

public class TransactionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public TransactionException(String message) {
        super(message);
    }
}
