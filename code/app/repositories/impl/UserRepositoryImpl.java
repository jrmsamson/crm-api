package repositories.impl;

import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.UserRequest;
import model.entities.UserResponse;
import model.jooq.tables.records.UserRecord;
import model.pojos.User;
import org.jooq.exception.DataAccessException;
import repositories.BaseRepository;
import repositories.UserRepository;
import org.jooq.Record3;
import org.jooq.SelectSelectStep;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.Tables.USER;


public class UserRepositoryImpl extends BaseRepositoryImpl implements UserRepository {

    public Optional<UUID> addUser(UserRequest user) {
        try {
            return doAddUser(user);
        } catch (DataAccessException exception) {
            if (isConstraintUserAndSurnameUniqueException(exception))
                throw new UserWithSameNameAndSurnameAlreadyExistException();
            throw exception;
        }
    }

    private boolean isConstraintUserAndSurnameUniqueException(DataAccessException exception) {
        return exception.getMessage().contains("user_name_surname_uindex");
    }

    private Optional<UUID> doAddUser(UserRequest user) {
        UserRecord userRecord = create.newRecord(USER, user);

        Optional<UserRecord> userCreated = create
                .insertInto(USER)
                .set(userRecord)
                .returning(USER.UUID)
                .fetchOptional();

        return userCreated.map(UserRecord::getUuid);
    }

    @Override
    public void editUser(UUID userUuid, UserRequest user) {
        create
                .update(USER)
                .set(USER.NAME, user.getName())
                .set(USER.SURNAME, user.getSurname())
                .where(USER.UUID.eq(userUuid))
                .execute();
    }

    @Override
    public void deleteUser(UUID userUuid) {
        create
                .update(USER)
                .set(USER.ACTIVE, Boolean.FALSE)
                .execute();
    }

    @Override
    public Optional<UserResponse> getUser(UUID userUuid) {
        return selectUser()
                .from(USER)
                .where(USER.ACTIVE.eq(Boolean.TRUE)
                        .and(
                                USER.UUID.eq(userUuid))
                )
                .fetchOptionalInto(UserResponse.class);
    }

    @Override
    public List<UserResponse> getUsersActive() {
        return selectUser()
                .from(USER)
                .where(USER.ACTIVE.eq(Boolean.TRUE))
                .fetchInto(UserResponse.class);
    }

    private SelectSelectStep<Record3<String, String, UUID>> selectUser() {
        return create
                .select(
                        USER.NAME,
                        USER.SURNAME,
                        USER.UUID
                );
    }
}
