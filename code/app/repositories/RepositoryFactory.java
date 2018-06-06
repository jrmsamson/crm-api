package repositories;

import org.jooq.DSLContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepositoryFactory {

    private DSLContext dslContext;

    @Inject
    private UserRepository userRepository;

    @Inject
    private LoginRepository loginRepository;

    @Inject
    private RoleRepository roleRepository;

    public void setDslContext(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public UserRepository getUserRepository() {
        userRepository.setDslContext(dslContext);
        return userRepository;
    }

    public LoginRepository getLoginRepository() {
        loginRepository.setDslContext(dslContext);
        return loginRepository;
    }

    public RoleRepository getRoleRepository() {
        roleRepository.setDslContext(dslContext);
        return roleRepository;
    }
}
