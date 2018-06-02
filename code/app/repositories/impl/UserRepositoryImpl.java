package repositories.impl;

import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.UserData;
import model.entities.UserResponse;
import model.jooq.tables.records.UserRecord;
import model.pojos.User;
import org.jooq.exception.DataAccessException;
import repositories.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.SelectSelectStep;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.Tables.USER;


public class UserRepositoryImpl implements UserRepository {

    public Optional<UUID> addUser(DSLContext dslContext, User user) {
        try {
            return doAddUser(dslContext, user);
        } catch (DataAccessException exception) {
            if (isConstraintUserAndSurnameUniqueException(exception))
                throw new UserWithSameNameAndSurnameAlreadyExistException();
            throw exception;
        }
    }

    private boolean isConstraintUserAndSurnameUniqueException(DataAccessException exception) {
        return exception.getMessage().contains("user_name_surname_uindex");
    }

    private Optional<UUID> doAddUser(DSLContext dslContext, User user) {
        UserRecord userRecord = dslContext.newRecord(USER, user);

        Optional<UserRecord> userCreated = dslContext.insertInto(USER)
                .set(userRecord)
                .returning(USER.UUID)
                .fetchOptional();

        return userCreated.map(UserRecord::getUuid);
    }

    @Override
    public void editUser(DSLContext dslContext, UUID userUuid, UserData user) {
        dslContext
                .update(USER)
                .set(USER.NAME, user.getName())
                .set(USER.SURNAME, user.getSurname())
                .where(USER.UUID.eq(userUuid))
                .execute();
    }

    @Override
    public void deleteUser(DSLContext dslContext, UUID userUuid) {
        dslContext
                .update(USER)
                .set(USER.ACTIVE, Boolean.FALSE)
                .execute();
    }

    @Override
    public Optional<UserResponse> getUser(DSLContext dslContext, UUID userUuid) {
        return selectUser(dslContext)
                .from(USER)
                .where(USER.ACTIVE.eq(Boolean.TRUE)
                        .and(
                                USER.UUID.eq(userUuid))
                )
                .fetchOptionalInto(UserResponse.class);
    }

    @Override
    public List<UserResponse> getUsersActive(DSLContext dslContext) {
        return selectUser(dslContext)
                .from(USER)
                .where(USER.ACTIVE.eq(Boolean.TRUE))
                .fetchInto(UserResponse.class);
    }

    private SelectSelectStep<Record3<String, String, UUID>> selectUser(DSLContext dslContext) {
        return dslContext
                .select(
                        USER.NAME,
                        USER.SURNAME,
                        USER.UUID
                );
    }
}
