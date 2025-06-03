package com.tcs.students.dto;

public class TokenResponse extends APIResponse {

    private String token;

    public TokenResponse(String status, Integer statusCode, String message, String token) {
        super(status, statusCode, message);
        this.token = token;
    }


    public String getToken() {
        return token;
    }
}
