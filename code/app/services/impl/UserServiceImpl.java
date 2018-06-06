package services.impl;

import enums.Role;
import exceptions.UserDoesNotExistException;
import exceptions.UserRequestException;
import model.entities.AddLoginRequest;
import model.entities.AddUserRequest;
import model.entities.UserResponse;
import model.pojos.User;
import repositories.RepositoryFactory;
import services.LoginService;
import services.RoleService;
import services.UserService;
import util.Notification;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

    private RoleService roleService;
    private LoginService loginService;

    @Inject
    public UserServiceImpl(RepositoryFactory repositoryFactory,
                           LoginService loginService,
                           RoleService roleService) {
        // For testing purpose
        super(repositoryFactory);
        this.loginService = loginService;
        this.roleService = roleService;
    }

    public Long addUser(AddUserRequest addUserRequest) {
        Notification userRequestNotification = addUserRequest.validation();
        if (userRequestNotification.hasErrors())
            throw new UserRequestException(userRequestNotification.errorMessage());

        User user = new User();
        user.setName(addUserRequest.getName());
        user.setSurname(addUserRequest.getSurname());
        user.setRoleId(roleService.getRoleId(addUserRequest.getRole()));

        Long userId = repositoryFactory.getUserRepository().addUser(user);
        AddLoginRequest addLoginRequest = new AddLoginRequest(addUserRequest.getUsername(), addUserRequest.getPassword());
        loginService.addLoginForUser(userId, addLoginRequest);

        return userId;
    }

    public void editUser(UUID userUuid, AddUserRequest addUserRequest) {
        repositoryFactory
                .getUserRepository()
                .editUser(userUuid, addUserRequest);
    }

    public void deleteUser(UUID userUuid) {
        repositoryFactory
                .getUserRepository()
                .deleteUser(userUuid);
    }

    public List<UserResponse> getUsersActive() {
        return repositoryFactory
                .getUserRepository()
                .getUsersActive();
    }

    public Role getUserRole(Long userId) {
        return repositoryFactory
                .getUserRepository()
                .getUserRoleByUserId(userId)
                .orElseThrow(UserDoesNotExistException::new);
    }
}
