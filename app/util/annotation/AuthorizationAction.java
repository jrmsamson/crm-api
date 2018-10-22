package util.annotation;

import enums.Role;
import exceptions.DatabaseCommitChangesException;
import exceptions.DatabaseConnectionException;
import exceptions.RoleDoesNotExistException;
import model.entities.responses.ErrorResponse;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.JDBCUtils;
import play.db.Database;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.UserService;
import util.Constants;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static util.Constants.ROLE_SESSION_KEY;
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
        if (hasValidToken(ctx) && isUserRolAllowed(ctx)) {
            saveUserIdIntoRequestContext(ctx);
            return delegate.call(ctx);
        }

        return CompletableFuture
                .completedFuture(unauthorized(Json.toJson(
                        new ErrorResponse("You're not allowed to access this resource.")
                )));
    }
    private boolean hasValidToken(Http.Context ctx) {

        Optional<Long> userId = getUserIdFromTheSession(ctx);
        Optional<String> token = getTokenFromTheSession(ctx);

        if (token.isPresent() && userId.isPresent()) {
            establishConnectionWithTheDatabase();
            connectJooqToTheDatabase();
            Boolean tokenValid = userService
                    .getUserTokenByUserId(userId.get())
                    .validate(token.get());

            if (tokenValid)
                renewUserToken(userId.get());

            commitDatabaseChanges();
            closeConnectionToTheDatabase();
            return tokenValid;
        }

        return false;
    }

    private boolean isUserRolAllowed(Http.Context ctx) {
        return getUserRole(ctx).filter(
                role -> Arrays.asList(configuration.rolesAllowed()).contains(role)
        ).isPresent();
    }

    private void renewUserToken(Long userId) {
        userService.renewUserToken(userId);
        commitDatabaseChanges();
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

    private Optional<Role> getUserRole(Http.Context ctx) {
        return Optional.ofNullable(
                ctx.session().get(ROLE_SESSION_KEY)
        ).flatMap(this::lookupRole);
    }

    private Optional<Role> lookupRole(String role) {
        try {
            return Optional.of(Role.lookup(role));
        } catch (RoleDoesNotExistException e) {
            return Optional.empty();
        }
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
            throw new DatabaseConnectionException(e);
        }
    }

    private void closeConnectionToTheDatabase() {
        JDBCUtils.safeClose(connection);
    }

    private void commitDatabaseChanges() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseCommitChangesException(e);
        }
    }

}
