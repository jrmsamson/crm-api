package services;

import model.entities.requests.AddEditLoginRequest;
import model.entities.requests.LoginRequest;
import model.entities.responses.UserSessionResponse;

public interface LoginService extends BaseService {

    UserSessionResponse login(LoginRequest loginRequest);

    void logout();

    void addLoginForUser(AddEditLoginRequest addEditLoginRequest);

    void editLogin(AddEditLoginRequest editPasswordRequest);

    void deleteLoginByUserId(Long userId);
}
