package com.tcs.students.dto;

public class TokenResponse extends APIResponse {

    private String token;

    public TokenResponse(String status, Integer statusCode, String message, String token) {
        super(status, statusCode, message);
        this.token = token;
    }

    private boolean isMFAEnabled;

    public boolean isMFAEnabled() {
        return isMFAEnabled;
    }

    public void setMFAEnabled(boolean MFAEnabled) {
        isMFAEnabled = MFAEnabled;
    }

    public String getToken() {
        return token;
    }
}
