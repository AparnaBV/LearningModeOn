package com.LearningModeOn.util;

import lombok.Getter;

@Getter
public enum ListItemFailureData {
    MISSING_MEDIA_ID("testInvalidItemsMissingMediaId.json"),
    MISSING_MEDIA_TYPE("testInvalidItemsMissingMediaType.json"),
    INVALID_MEDIA_TYPE("testInvalidItemMediaType.json"),
    INVALID_MEDIA_ID("testInvalidItemMediaId.json");

    private String ingestedItem;

    ListItemFailureData(String ingestedItem) {
        this.ingestedItem = ingestedItem;
    }
}
