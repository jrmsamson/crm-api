package repositories.impl;

import enums.Role;
import model.entities.EditUser;
import model.entities.AddUser;
import model.entities.NewToken;
import model.entities.responses.UserResponse;
import model.entities.responses.UserTokenResponse;
import model.pojos.User;
import org.jooq.*;
import org.jooq.impl.DSL;
import repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.Tables.ROLE;
import static model.jooq.Tables.USER;

public class UserRepositoryImpl extends BaseRepositoryImpl implements UserRepository {

    public Optional<UserResponse> getUserByUuid(UUID userUuid) {
        return selectUser()
                .where(USER.ACTIVE.eq(Boolean.TRUE)
                        .and(USER.UUID.eq(userUuid)))
                .fetchOptional(record -> new UserResponse(
                        record.value1(),
                        record.value2(),
                        record.value3(),
                        Role.lookup(record.value4())
                ));
    }

    public UUID addUser(AddUser user) {
        return create
                .insertInto(USER)
                .set(USER.NAME, user.getName())
                .set(USER.SURNAME, user.getSurname())
                .set(USER.ROLE_ID, buildRoleIdQuery(user.getRole()))
                .returning(USER.UUID)
                .fetchOne()
                .getUuid();
    }

    public void editUser(EditUser user) {
        create
                .update(USER)
                .set(USER.NAME, user.getName())
                .set(USER.SURNAME, user.getSurname())
                .set(USER.ROLE_ID, buildRoleIdQuery(user.getRole()))
                .where(USER.UUID.eq(user.getUuid()))
                .execute();
    }

    private SelectConditionStep<Record1<Integer>> buildRoleIdQuery(Role role) {
        return create.select(ROLE.ID)
                .from(ROLE)
                .where(ROLE.NAME.lower().eq(
                        role.getName().toLowerCase())
                );
    }

    public void deleteUserByUuid(UUID userUuid) {
        create
                .update(USER)
                .set(USER.ACTIVE, Boolean.FALSE)
                .execute();
    }

    public List<UserResponse> getUsersActive(Long currentUserId) {
        return selectUser()
                .where(USER.ACTIVE.eq(Boolean.TRUE))
                .and(USER.ID.notEqual(currentUserId))
                .fetch(record -> new UserResponse(
                        record.value1(),
                        record.value2(),
                        record.value3(),
                        Role.lookup(record.value4())
                ));
    }

    public Optional<Role> getUserRoleByUserId(Long userId) {
        return create
                .select(ROLE.NAME)
                .from(USER)
                .innerJoin(ROLE).on(USER.ROLE_ID.eq(ROLE.ID))
                .where(USER.ID.eq(userId)
                        .and(USER.ACTIVE.eq(Boolean.TRUE))
                ).fetchOptional(record -> Role.lookup(record.value1()));
    }

    public Optional<UserTokenResponse> getUserTokenByUserId(Long userId) {
        return create
                .select(USER.TOKEN, USER.TOKEN_EXPIRATION)
                .from(USER)
                .where(USER.ID.eq(userId)
                        .and(USER.ACTIVE.eq(Boolean.TRUE))
                ).fetchOptionalInto(UserTokenResponse.class);
    }

    public void updateUserToken(Long userId, NewToken token) {
        create.update(USER)
                .set(USER.TOKEN, token.getToken())
                .set(USER.TOKEN_EXPIRATION, token.getTokenExpiration())
                .where(USER.ID.eq(userId))
                .execute();
    }

    public void removeUserToken(Long currentUserId) {
        create.update(USER)
                .set(USER.TOKEN, DSL.val((String) null))
                .set(USER.TOKEN_EXPIRATION, DSL.val((LocalDateTime) null))
                .execute();
    }

    public void updateUserTokenExpirationByUserId(Long userId, LocalDateTime tokenExpiration) {
        create.update(USER)
                .set(USER.TOKEN_EXPIRATION, tokenExpiration)
                .where(USER.ID.eq(userId))
                .execute();
    }

    public Optional<Long> getUserIdByUuid(UUID uuid) {
        return create
                .select(USER.ID)
                .from(USER)
                .where(USER.UUID.eq(uuid)
                        .and(USER.ACTIVE.eq(Boolean.TRUE))
                )
                .fetchOptionalInto(Long.class);
    }

    public Optional<UserResponse> getUserByNameAndSurname(String name, String surname) {
        return selectUser()
                .where(
                        USER.NAME.eq(name)
                                .and(USER.SURNAME.eq(surname))
                )
                .fetchOptionalInto(UserResponse.class);
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
}
