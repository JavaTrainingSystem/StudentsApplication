package com.tcs.students.exceptions.handlers;

import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<APIResponse> handleFileNotFound(FileNotFoundException e) {
        return new ResponseEntity<>(new APIResponse(CommonConstants.FAILED, 400, "File is not there in the system"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<APIResponse> handleFileNotFound(BadRequestException e) {
        return new ResponseEntity<>(new APIResponse(CommonConstants.FAILED, 400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }


}
