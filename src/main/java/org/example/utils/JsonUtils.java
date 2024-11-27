package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }
}
