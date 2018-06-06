package services;

import model.entities.LoginRequest;

public interface AuthenticationService extends BaseService {

    String login(LoginRequest loginRequest);
}
