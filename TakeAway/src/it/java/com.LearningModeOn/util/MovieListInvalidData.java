package com.LearningModeOn.util;

import lombok.Getter;

@Getter
public enum MovieListInvalidData {
    INVALID_ISO_639("testInvalidListBadISO639.json","hello"),
    MISSING_ISO_639("testInvalidListMissingISO639.json","iso_639_1 must be provided"),
    MISSING_NAME("testInvalidListMissingName.json","name must be provided");

    private String ingestedFile;
    private String errorMessage;

    MovieListInvalidData(String ingestedFile, String errorMessage) {
        this.ingestedFile = ingestedFile;
        this.errorMessage = errorMessage;
    }
}
