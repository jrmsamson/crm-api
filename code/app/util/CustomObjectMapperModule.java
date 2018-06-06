package util;

import com.google.inject.AbstractModule;

public class CustomObjectMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CustomObjectMapper.class).asEagerSingleton();
    }

}