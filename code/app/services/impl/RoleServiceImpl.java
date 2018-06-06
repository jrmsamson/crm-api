package services.impl;

import enums.Role;
import exceptions.RoleDoesNotExistException;
import repositories.RepositoryFactory;
import services.RoleService;

import javax.inject.Inject;

public class RoleServiceImpl extends BaseServiceImpl implements RoleService {

    @Inject
    public RoleServiceImpl(RepositoryFactory repositoryFactory) {
        super(repositoryFactory);
    }

    public Integer getRoleId(Role role) {
        return repositoryFactory
                .getRoleRepository()
                .getRoleId(role)
                .orElseThrow(RoleDoesNotExistException::new);
    }
}
