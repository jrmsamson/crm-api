package repositories;

import model.entities.LoginResponse;
import model.pojos.Login;

import java.util.Optional;
import java.util.UUID;

public interface LoginRepository extends BaseRepository {

    void addLogin(Login login);

    Optional<LoginResponse> getLoginByUsername(String username);

    void editLoginPassword(Long userId, String password);

    void deleteLoginByUserId(Long userId);

    Optional<UUID> getLoginPasswordSaltByUserId(Long userId);
}
