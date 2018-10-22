package exceptions;

public class TransactionConfigurationException extends RuntimeException {
    public TransactionConfigurationException() {
        super("Error trying to get the transaction configuration");
    }
}
