package util;

import com.google.inject.AbstractModule;
import repositories.CustomerRepository;
import repositories.LoginRepository;
import repositories.RoleRepository;
import repositories.UserRepository;
import repositories.impl.CustomerRepositoryImpl;
import repositories.impl.LoginRepositoryImpl;
import repositories.impl.RoleRepositoryImpl;
import repositories.impl.UserRepositoryImpl;
import services.*;
import services.impl.*;

public class GuiceDependencyInjectionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(LoginService.class).to(LoginServiceImpl.class);
        bind(LoginRepository.class).to(LoginRepositoryImpl.class);
        bind(RoleRepository.class).to(RoleRepositoryImpl.class);
        bind(CustomerRepository.class).to(CustomerRepositoryImpl.class);
        bind(CustomerService.class).to(CustomerServiceImpl.class);
    }
}