package util.annotation;

import exceptions.DatabaseException;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import play.db.Database;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.UserService;
import util.Constants;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static util.Constants.USER_ID_SESSION_KEY;

public class AuthorizationAction extends Action<Secured> {

    private Database database;
    private UserService userService;
    private Connection connection;

    @Inject
    public AuthorizationAction(Database database, UserService userService) {
        this.database = database;
        this.userService = userService;
    }

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        if (hasValidToken(ctx)) {
            saveUserIdIntoRequestContext(ctx);
            return delegate.call(ctx);
        }

        return CompletableFuture
                .completedFuture(unauthorized("Invalid credentials"));
    }

    private boolean hasValidToken(Http.Context ctx) {

        Optional<Long> userId = getUserIdFromTheSession(ctx);
        Optional<String> token = getTokenFromTheSession(ctx);

        if (token.isPresent() && userId.isPresent()) {
            establishConnectionWithTheDatabase();
            connectJooqToTheDatabase();
            return userService
                    .getUserToken(userId.get())
                    .validate(token.get());
        }

        return false;
    }


    private void saveUserIdIntoRequestContext(Http.Context ctx) {
        getUserIdFromTheSession(ctx)
                .ifPresent(currentUserId -> ctx.args.put(Constants.REQUEST_CONTEXT_USER_ID, currentUserId));
    }

    private Optional<Long> getUserIdFromTheSession(Http.Context ctx) {
        return Optional.ofNullable(
                ctx.session().get(USER_ID_SESSION_KEY)
        ).flatMap(userId -> Optional.of(
                Long.valueOf(userId)
        ));
    }

    private Optional<String> getTokenFromTheSession(Http.Context ctx) {
        return Optional.ofNullable(ctx.session().get(Constants.TOKEN_SESSION_KEY));
    }

    private void connectJooqToTheDatabase() {
        this.userService.setTransaction(
                DSL.using(connection, SQLDialect.POSTGRES, new Settings())
        );
    }

    private void establishConnectionWithTheDatabase() {
        try {
            connection = database.getDataSource().getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException("Error trying to establish connection with the database", e);
        }
    }

}
