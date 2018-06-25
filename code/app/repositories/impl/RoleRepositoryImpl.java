package repositories.impl;

import model.pojos.Role;
import repositories.RoleRepository;

import java.util.Optional;

import static model.jooq.tables.Role.ROLE;

public class RoleRepositoryImpl extends BaseRepositoryImpl implements RoleRepository {

    public Optional<Role> getById(Integer roleId) {
        return create
                .selectFrom(ROLE)
                .where(ROLE.ID.eq(roleId))
                .fetchOptionalInto(Role.class);
    }

    public Optional<Role> getByName(String name) {
        return create
                .selectFrom(ROLE)
                .where(
                        ROLE.NAME.lower()
                                .eq(name.toLowerCase())

                ).fetchOptionalInto(Role.class);
    }
}
