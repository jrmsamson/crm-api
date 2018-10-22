package services;

import model.entities.requests.AddEditLoginRequest;
import model.entities.requests.LoginRequest;
import model.entities.responses.UserSession;

public interface LoginService extends BaseService {

    UserSession login(LoginRequest loginRequest);

    void logout();

    void addLoginForUser(AddEditLoginRequest addEditLoginRequest);

    void editLogin(AddEditLoginRequest editPasswordRequest);

    void deleteLoginByUserId(Long userId);
}
