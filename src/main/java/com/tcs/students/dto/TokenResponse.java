package com.tcs.students.dto;

public class TokenResponse extends APIResponse {

    private String token;

    private String profilePhoto;

    public TokenResponse(String status, Integer statusCode, String message, String token, String profilePhoto) {
        super(status, statusCode, message);
        this.token = token;
        this.profilePhoto = profilePhoto;
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

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
