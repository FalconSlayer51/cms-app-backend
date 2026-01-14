package org.ramesh.backend.domain.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final Map<String, String> fieldErrors;
    private final OffsetDateTime timestamp;

    public ErrorResponse(int status, String error, String message, String path, Map<String, String> fieldErrors, OffsetDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}

