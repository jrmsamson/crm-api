package exceptions;

import java.sql.SQLException;

public class DatabaseCommitChangesException extends RuntimeException {
    public DatabaseCommitChangesException(SQLException e) {
        super("Error trying to commit the database changes", e);
    }
}
