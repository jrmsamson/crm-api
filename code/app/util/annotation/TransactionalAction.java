package util.annotation;

import exceptions.DatabaseCommitChangesException;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseRollbackChangesException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.JDBCUtils;
import play.db.Database;
import play.mvc.Action;
import play.mvc.Result;
import util.Constants;
import util.HttpExceptionHandler;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletionStage;
import static play.mvc.Http.Context;

public class TransactionalAction extends Action<Transactional> {

    private HttpExceptionHandler httpExceptionHandler;

    private Database database;

    private Connection connection;

    @Inject
    public TransactionalAction(Database database, HttpExceptionHandler httpExceptionHandler) {
        this.database = database;
        this.httpExceptionHandler = httpExceptionHandler;
    }

    public CompletionStage<Result> call(Context context) {

        establishConnectionWithTheDatabase();

        return connectJooqToTheDatabase().transactionResultAsync(configuration -> {

            saveDslContextIntoTheRequestTransaction(context, DSL.using(configuration));

            return delegate.call(context);

        }).thenComposeAsync(resultCompletionStage -> resultCompletionStage.thenApply(result -> {

            commitDatabaseChanges();

            return result;

        })).exceptionally(throwable -> {

            rollbackDatabaseChanges();

            return httpExceptionHandler.handle(throwable);
        });
    }

    private void establishConnectionWithTheDatabase() {
        try {
            connection = database.getDataSource().getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    private DSLContext connectJooqToTheDatabase() {
        return DSL.using(connection, SQLDialect.POSTGRES, new Settings());
    }

    private void saveDslContextIntoTheRequestTransaction(Context context, DSLContext dslContext) {
        context.args.put(Constants.REQUEST_TRANSACTION_DSL_CONTEXT, dslContext);
    }

    private void commitDatabaseChanges() {
        try {
            connection.commit();
            closeConnectionToTheDatabase();
        } catch (SQLException e) {
            throw new DatabaseCommitChangesException(e);
        }
    }

    private void rollbackDatabaseChanges() {
        try {
            connection.rollback();
            closeConnectionToTheDatabase();
        } catch (SQLException e) {
            throw new DatabaseRollbackChangesException(e);
        }
    }

    private void closeConnectionToTheDatabase() {
        JDBCUtils.safeClose(this.connection);
    }

}
