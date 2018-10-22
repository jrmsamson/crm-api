package repositories;

import model.pojos.Role;

import java.util.Optional;

public interface RoleRepository extends BaseRepository {

    Optional<Role> getById(Integer roleId);

    Optional<Role> getByName(String name);
}
