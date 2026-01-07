package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperConfig {

    private static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper();
        INSTANCE.registerModule(new JavaTimeModule());
        INSTANCE.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private ObjectMapperConfig(){}

    public static ObjectMapper getMapper(){
        return INSTANCE;
    }
}
