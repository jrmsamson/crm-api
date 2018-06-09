package repositories.impl;

import enums.Role;
import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.*;
import model.pojos.User;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.Tables.ROLE;
import static model.jooq.Tables.USER;

public class UserRepositoryImpl extends BaseRepositoryImpl implements UserRepository {

    private static final String USER_NAME_SURNAME_DB_CONSTRAINT = "user_name_surname_uindex";

    public Optional<UserResponse> getUserById(Long userId) {
        return selectUser()
                .where(USER.ID.eq(userId))
                .fetchOptionalInto(UserResponse.class);
    }

    public Optional<UserResponse> getUserByUuid(UUID userUuid) {
        return selectUser()
                .where(USER.ACTIVE.eq(Boolean.TRUE)
                        .and(USER.UUID.eq(userUuid)
                        )).fetchOptional(record -> new UserResponse(
                        record.value1(),
                        record.value2(),
                        record.value3(),
                        Role.lookup(record.value4())
                ));
    }

    public Long addUser(User user) {
        try {
            return doAddUser(user);
        } catch (DataAccessException exception) {
            if (isConstraintUserAndSurnameUniqueException(exception))
                throw new UserWithSameNameAndSurnameAlreadyExistException();
            throw exception;
        }
    }

    private boolean isConstraintUserAndSurnameUniqueException(DataAccessException exception) {
        return exception.getMessage().contains(USER_NAME_SURNAME_DB_CONSTRAINT);
    }

    private Long doAddUser(User user) {
        return create
                .insertInto(USER)
                .set(create.newRecord(USER, user))
                .returning(USER.ID)
                .fetchOne()
                .getId();
    }

    @Override
    public void editUser(UUID userUuid, AddUserRequest user) {
        create
                .update(USER)
                .set(USER.NAME, user.getName())
                .set(USER.SURNAME, user.getSurname())
                .set(USER.ROLE_ID, getRoleId(user.getRole()))
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
    public List<UserResponse> getUsersActive() {
        return selectUser()
                .where(USER.ACTIVE.eq(Boolean.TRUE))
                .fetch(record -> new UserResponse(
                        record.value1(),
                        record.value2(),
                        record.value3(),
                        Role.lookup(record.value4())
                ));
    }

    @Override
    public Optional<Role> getUserRoleByUserId(Long userId) {
        return create
                .select(ROLE.NAME)
                .from(USER)
                .innerJoin(ROLE).on(USER.ROLE_ID.eq(ROLE.ID))
                .where(USER.ID.eq(userId))
                .fetchOptional(record -> Role.lookup(record.value1()));
    }

    @Override
    public Optional<UserTokenResponse> getUserToken(Long userId) {
        return create
                .select(USER.TOKEN, USER.TOKEN_EXPIRATION)
                .from(USER)
                .where(USER.ID.eq(userId))
                .fetchOptionalInto(UserTokenResponse.class);
    }

    public void updateUserToken(UpdateUserTokenRequest updateUserTokenRequest) {
        create.update(USER)
                .set(USER.TOKEN, updateUserTokenRequest.getToken())
                .set(USER.TOKEN_EXPIRATION, updateUserTokenRequest.getTokenExpiration())
                .where(USER.ID.eq(updateUserTokenRequest.getUserId()))
                .execute();
    }

    @Override
    public void removeUserToken(Long currentUserId) {
        create.update(USER)
                .set(USER.TOKEN, DSL.val((String) null))
                .set(USER.TOKEN_EXPIRATION, DSL.val((LocalDateTime) null))
                .execute();
    }

    @Override
    public void updateUserTokenExpiration(UpdateUserTokenExpiration updateUserTokenExpiration) {
        create.update(USER)
                .set(USER.TOKEN_EXPIRATION, updateUserTokenExpiration.getTokenExpiration())
                .where(USER.ID.eq(updateUserTokenExpiration.getUserId()))
                .execute();
    }

    private SelectOnConditionStep<Record4<String, String, UUID, String>> selectUser() {
        return create
                .select(
                        USER.NAME,
                        USER.SURNAME,
                        USER.UUID,
                        ROLE.NAME
                )
                .from(USER)
                .innerJoin(ROLE).on(USER.ROLE_ID.eq(ROLE.ID));
    }

    private SelectConditionStep<Record1<Integer>> getRoleId(Role role) {
        return create
                .select(ROLE.ID)
                .from(ROLE)
                .where(
                        ROLE.NAME.lower().eq(
                                role.name().toLowerCase()
                        )
                );
    }
}
