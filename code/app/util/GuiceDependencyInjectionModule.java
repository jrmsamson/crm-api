package util;

import com.google.inject.AbstractModule;
import repositories.UserRepository;
import repositories.impl.UserRepositoryImpl;
import services.UserService;
import services.impl.UserServiceImpl;

public class GuiceDependencyInjectionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);
    }
}