package exceptions;

public class TokenGenerationException extends RuntimeException {

    public TokenGenerationException(Exception e) {
        super(e);
    }
}
