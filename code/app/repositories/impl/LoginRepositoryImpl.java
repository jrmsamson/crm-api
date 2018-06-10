package repositories.impl;

import exceptions.LoginNotExistException;
import exceptions.LoginWithTheSameUsernameAlreadyExistException;
import model.entities.LoginResponse;
import model.jooq.tables.records.LoginRecord;
import model.pojos.Login;
import org.jooq.UpdateSetFirstStep;
import org.jooq.exception.DataAccessException;
import repositories.LoginRepository;

import javax.xml.crypto.Data;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.tables.Login.LOGIN;

public class LoginRepositoryImpl extends BaseRepositoryImpl implements LoginRepository {

    private static final String LOGIN_USERNAME_DB_CONSTRAINT = "login_username_uindex";

    public void addLogin(Login login) {
        try {
            doAddLogin(login);
        } catch (DataAccessException exception) {
            if (isConstraintUsernameUniqueException(exception))
                throw new LoginWithTheSameUsernameAlreadyExistException();
            throw exception;
        }
    }

    private void doAddLogin(Login login) {
        create.insertInto(LOGIN)
                .set(create.newRecord(LOGIN, login))
                .execute();
    }

    public Optional<LoginResponse> getLoginByUsername(String username) {
        return create
                .select(
                        LOGIN.USER_ID,
                        LOGIN.PASSWORD,
                        LOGIN.PASSWORD_SALT
                ).from(LOGIN)
                .where(LOGIN.ACTIVE.eq(Boolean.TRUE)
                        .and(LOGIN.USERNAME.eq(username))
                )
                .fetchOptionalInto(LoginResponse.class);
    }

    @Override
    public void editLogin(Login login) {
        try {
            doEditLogin(login);
        } catch (DataAccessException e) {
            if (isConstraintUsernameUniqueException(e))
                throw new LoginWithTheSameUsernameAlreadyExistException();
            throw e;
        }
    }

    private void doEditLogin(Login login) {
        create.update(LOGIN)
                .set(create.newRecord(LOGIN, login))
                .where(LOGIN.USER_ID.eq(login.getUserId()))
                .execute();
    }

    private boolean isConstraintUsernameUniqueException(DataAccessException exception) {
        return exception.getMessage().contains(LOGIN_USERNAME_DB_CONSTRAINT);
    }

    @Override
    public void deleteLoginByUserId(Long userId) {
        create.update(LOGIN)
                .set(LOGIN.ACTIVE, Boolean.FALSE)
                .where(LOGIN.USER_ID.eq(userId))
                .execute();
    }

    @Override
    public Optional<UUID> getLoginPasswordSaltByUserId(Long userId) {
        return create.select(LOGIN.PASSWORD_SALT)
                .from(LOGIN)
                .where(LOGIN.USER_ID.eq(userId))
                .fetchOptionalInto(UUID.class);
    }
}
