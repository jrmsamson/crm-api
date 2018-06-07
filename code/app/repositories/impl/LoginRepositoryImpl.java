package repositories.impl;

import model.entities.LoginResponse;
import model.pojos.Login;
import repositories.LoginRepository;

import java.util.Optional;
import java.util.UUID;

import static model.jooq.tables.Login.LOGIN;

public class LoginRepositoryImpl extends BaseRepositoryImpl implements LoginRepository {

    public void addLogin(Login login) {
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
    public void editLoginPassword(Long userId, String password) {
        create.update(LOGIN)
                .set(LOGIN.PASSWORD, password)
                .execute();
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
