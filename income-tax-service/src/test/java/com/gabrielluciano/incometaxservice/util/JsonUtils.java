package com.gabrielluciano.incometaxservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public static String asJsonString(final Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }
}
