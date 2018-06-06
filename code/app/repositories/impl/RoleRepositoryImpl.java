package repositories.impl;

import enums.Role;
import repositories.RoleRepository;

import java.util.Optional;

import static model.jooq.tables.Role.ROLE;

public class RoleRepositoryImpl extends BaseRepositoryImpl implements RoleRepository {

    public Optional<Integer> getRoleId(Role name) {
        return create
                .select(ROLE.ID)
                .from(ROLE)
                .where(ROLE.NAME.lower().eq(name.getName().toLowerCase()))
                .fetchOptionalInto(Integer.class);
    }

}
