package util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import enums.Role;
import exceptions.RoleDeserializationException;

public class RoleDeserializer extends JsonDeserializer<Role> {

    @Override
    public Role deserialize(JsonParser jsonParser, DeserializationContext ctxt) {
        try {
            return Role.lookup(jsonParser.getText());
        } catch (Exception e) {
            throw new RoleDeserializationException(e);
        }
    }
}
