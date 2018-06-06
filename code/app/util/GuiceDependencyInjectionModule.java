package util;

import com.google.inject.AbstractModule;
import repositories.LoginRepository;
import repositories.RoleRepository;
import repositories.UserRepository;
import repositories.impl.LoginRepositoryImpl;
import repositories.impl.RoleRepositoryImpl;
import repositories.impl.UserRepositoryImpl;
import services.AuthenticationService;
import services.LoginService;
import services.RoleService;
import services.UserService;
import services.impl.AuthenticationServiceImpl;
import services.impl.LoginServiceImpl;
import services.impl.RoleServiceImpl;
import services.impl.UserServiceImpl;

public class GuiceDependencyInjectionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
        bind(LoginService.class).to(LoginServiceImpl.class);
        bind(LoginRepository.class).to(LoginRepositoryImpl.class);
        bind(RoleRepository.class).to(RoleRepositoryImpl.class);
        bind(RoleService.class).to(RoleServiceImpl.class);
    }
}