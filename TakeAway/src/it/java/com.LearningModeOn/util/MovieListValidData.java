package com.LearningModeOn.util;

import lombok.Getter;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum MovieListValidData {

    VALID_LIST("testValidList.json"),
    VALID_ALL_DEFAULTS_LIST("testValidListDefaults.json"),
    VALID_REQ_FIELDS_ONLY_LIST("testValidListReqFieldsOnly.json");

    private String ingestedFile;

    MovieListValidData(String ingestedFile) {
        this.ingestedFile = ingestedFile;
    }
}
