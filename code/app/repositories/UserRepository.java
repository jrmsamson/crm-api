package repositories;

import model.pojos.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository {

    Optional<User> getById(Long id);

    Optional<User> getByUuid(UUID uuid);

    List<User> getActive(Long excludedUserId);

    Optional<User> getByNameAndSurname(String name, String surname);

    UUID add(User user);

    void update(User user);

    void deleteByUuid(UUID uuid);
}
