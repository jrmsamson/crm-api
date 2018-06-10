package services;

import model.entities.AddEditLogin;
import model.entities.LoginRequest;
import model.entities.UserSession;

public interface LoginService extends BaseService {

    UserSession login(LoginRequest loginRequest);

    void logout();

    void addLoginForUser(AddEditLogin addEditLogin);

    void editLogin(AddEditLogin editPasswordRequest);

    void deleteLoginByUserId(Long userId);
}
