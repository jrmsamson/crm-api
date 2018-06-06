package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enums.Role;
import play.libs.Json;

public class CustomObjectMapper {

    CustomObjectMapper() {
        ObjectMapper mapper = Json.newDefaultMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Role.class, new RoleDeserializer());
        mapper.registerModule(module);
        Json.setObjectMapper(mapper);
    }

}