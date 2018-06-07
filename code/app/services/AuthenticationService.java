package services;

import model.entities.LoginRequest;
import model.entities.UserSession;

public interface AuthenticationService extends BaseService {

    UserSession login(LoginRequest loginRequest);

    void logout();
}
