package services;

import model.entities.AddLoginRequest;

public interface LoginService extends BaseService {

    void addLoginForUser(Long userId, AddLoginRequest addLoginRequest);

    void editLoginPassword(Long userId, String password);
}
