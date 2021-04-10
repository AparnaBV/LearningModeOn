package com.LearningModeOn.util;

import lombok.Getter;

@Getter
public enum Status {
    SUCCESS_ACCEPTED(201, "The item/record was created successfully."),
    SUCCESS_UPDATE_ACCEPTED(201, "The item/record was updated successfully."),
    SUCCESS(200, "The item/record was deleted successfully."),
    ERROR_ACCESS(401, "This token hasn't been granted write permission by the user."),
    ERROR_BAD_KEY(401, "Invalid API key: You must be granted a valid key."),
    ERROR_BAD_RESOURCE(404, "The resource you requested could not be found."),
    INTERNAL_ERROR(500, "Internal error: Something went wrong, contact TMDb."),
    ERROR_MISSING_REQUIRED_DATA(422, "must be provided"),
    ERROR_SCOPE(401, "No matching scope found.");

    private int code;
    private String message;

    Status(int error_code, String errorMessage) {
        this.code = error_code;
        this.message = errorMessage;
    }
}
