package repositories.impl;

import exceptions.UserRepositoryException;
import model.pojos.User;
import repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.Tables.USER;

public class UserRepositoryImpl extends BaseRepositoryImpl implements UserRepository {

    public Optional<User> getById(Long id) {
        return create
                .selectFrom(USER)
                .where(USER.ID.eq(id))
                .fetchOptionalInto(User.class);
    }

    public Optional<User> getByUuid(UUID uuid) {
        return create
                .selectFrom(USER)
                .where(USER.ACTIVE.eq(Boolean.TRUE)
                        .and(USER.UUID.eq(uuid)))
                .fetchOptionalInto(User.class);
    }


    public List<User> getActive(Long excludedUserId) {
        return create
                .selectFrom(USER)
                .where(USER.ACTIVE.eq(Boolean.TRUE))
                .and(USER.ID.notEqual(excludedUserId))
                .fetchInto(User.class);
    }

    public Optional<User> getByNameAndSurname(String name, String surname) {
        return create
                .selectFrom(USER)
                .where(
                        USER.NAME.eq(name)
                                .and(USER.SURNAME.eq(surname))
                )
                .fetchOptionalInto(User.class);
    }

    public UUID add(User user) {
        try {
            return doAdd(user);
        } catch (Exception exception) {
            throw new UserRepositoryException(exception);
        }
    }

    private UUID doAdd(User user) {
        return create
                .insertInto(USER)
                .set(USER.NAME, user.getName())
                .set(USER.SURNAME, user.getSurname())
                .set(USER.ROLE_ID, user.getRoleId())
                .returning(USER.UUID)
                .fetchOne()
                .getUuid();
    }

    public void update(User user) {
        create
                .update(USER)
                .set(USER.NAME, user.getName())
                .set(USER.SURNAME, user.getSurname())
                .set(USER.ROLE_ID, user.getRoleId())
                .set(USER.TOKEN, user.getToken())
                .set(USER.TOKEN_EXPIRATION, user.getTokenExpiration())
                .where(USER.UUID.eq(user.getUuid()))
                .execute();
    }

    public void deleteByUuid(UUID uuid) {
        create
                .update(USER)
                .set(USER.ACTIVE, Boolean.FALSE)
                .where(USER.UUID.eq(uuid))
                .execute();
    }

}
