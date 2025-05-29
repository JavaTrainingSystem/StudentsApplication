package com.tcs.students.dto;

public class APIResponse {

    private String status;

    private Integer statusCode;

    private String message;

    public APIResponse(String status, Integer statusCode, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
