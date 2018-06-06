package exceptions;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(Exception e) {
        super(e);
    }
}
