package com.LearningModeOn.util;

import com.LearningModeOn.base.Credentials;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class TestHelper {

    public static Object getResponseObject(String responseString, Class responseClass)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(responseString, responseClass);
    }

    public static Object getResponseObjFromFile(File file, Class responseClass)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(file, responseClass);
    }

    public static String getJsonString(Object o)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return objectMapper.writeValueAsString(o);
    }

    public static String readFileAsString(String file)
            throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public static String getPasskeyFilePath(String fileName) {
        return System.getProperty("user.dir") + "/src/it/resources/" + fileName;
    }

    public static String getTestDataFilePath(String fileName) {
        return System.getProperty("user.dir") + "/src/it/resources/test-data/" + fileName;
    }

    public static Credentials getAccessCredentials(File jsonFile)
            throws IOException {
        return (Credentials) getResponseObjFromFile(jsonFile, Credentials.class);
    }

}
