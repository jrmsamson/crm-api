package exceptions;

public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(Throwable cause) {
        super("Error trying to establish connection with the database", cause);
    }
}
