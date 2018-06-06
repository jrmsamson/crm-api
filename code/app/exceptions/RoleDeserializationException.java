package exceptions;

public class RoleDeserializationException extends RuntimeException {

    public RoleDeserializationException(Exception e) {
        super(e);
    }
}
