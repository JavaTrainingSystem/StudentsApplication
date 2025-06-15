package com.tcs.students.exceptions.handlers;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String message) {
        super(message);
    }

}
