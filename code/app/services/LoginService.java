package services;

import model.entities.AddLoginRequest;
import model.entities.EditPasswordRequest;
import model.entities.LoginRequest;
import model.entities.UserSession;

public interface LoginService extends BaseService {

    UserSession login(LoginRequest loginRequest);

    void logout();

    void addLoginForUser(AddLoginRequest addLoginRequest);

    void editLoginPassword(EditPasswordRequest editPasswordRequest);

    void deleteLoginByUserId(Long userId);
}
