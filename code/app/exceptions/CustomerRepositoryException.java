package exceptions;

public class CustomerRepositoryException extends RuntimeException {

    public CustomerRepositoryException(Throwable cause) {
        super(cause);
    }
}
