package com.tcs.students.dto;

import java.util.List;

public class EmailPayload {

    private List<String> to;

    private String body;

    private String subject;

    public EmailPayload(List<String> to, String body, String subject) {
        this.to = to;
        this.body = body;
        this.subject = subject;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
