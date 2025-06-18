package com.tcs.students.exceptions.handlers;

import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseEntity<APIResponse> handleFileNotFound(UnAuthorizedException e) {
        return new ResponseEntity<>(new APIResponse(CommonConstants.FAILED, 401, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFoundError(NoResourceFoundException ex) {
        return "redirect:/static/notfound.html";
    }
}
