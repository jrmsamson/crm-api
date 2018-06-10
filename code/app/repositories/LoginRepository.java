package repositories;

import model.entities.responses.LoginResponse;
import model.pojos.Login;

import java.util.Optional;
import java.util.UUID;

public interface LoginRepository extends BaseRepository {

    void addLogin(Login login);

    Optional<LoginResponse> getLoginByUsername(String username);

    void editLogin(Login login);

    void deleteLoginByUserId(Long userId);

    Optional<UUID> getLoginPasswordSaltByUserId(Long userId);
}
