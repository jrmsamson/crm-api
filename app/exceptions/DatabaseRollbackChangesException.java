package exceptions;

import java.sql.SQLException;

public class DatabaseRollbackChangesException extends RuntimeException {
    public DatabaseRollbackChangesException(SQLException e) {
        super("Error trying to rollback the database changes", e);
    }
}
