package com.tcs.students.exceptions.handlers;

import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
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

    @org.springframework.web.bind.annotation.ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNotFoundError(NoResourceFoundException ex) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("notfound.html"); // This is the name of the static page (HTML file)
        mav.setStatus(HttpStatus.OK);
        return mav;
    }

}
