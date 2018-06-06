package repositories;

import enums.Role;

import java.util.Optional;

public interface RoleRepository extends BaseRepository {

    Optional<Integer> getRoleId(Role role);
}
