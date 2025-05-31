package com.tcs.students.controllers;

import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dto.APIResponse;
import com.tcs.students.dto.Student;
import com.tcs.students.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("student")
public class StudentController {

    private StudentService service;


    @Autowired
    public void setService(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public Student saveStudent(@RequestBody Student student) {
        return service.saveStudent(student);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        return new ResponseEntity<>(service.getStudents(), HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<Map> deleteStudent(@RequestParam Integer studentId) {
        service.deleteStudent(studentId);
        return new ResponseEntity<>(Map.of("status", "SUCCESS"), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        return new ResponseEntity<>(service.updateStudent(student), HttpStatus.OK);
    }

    @PostMapping("upload")
    public ResponseEntity<APIResponse> uploadStudentDoc(@RequestPart MultipartFile file, @RequestParam Integer studentId) {
        return new ResponseEntity<>(service.uploadStudentDoc(file, studentId), HttpStatus.OK);
    }

    @GetMapping("download")
    public ResponseEntity<?> downloadStudentDoc(@RequestParam Integer studentId) throws IOException {
        Map<String, Object> inputStreamResource = service.downloadStudentDoc(studentId);

        if (CommonConstants.FAILED.equals(inputStreamResource.get("status"))) {
            return new ResponseEntity<>(inputStreamResource, HttpStatus.BAD_REQUEST);
        }

        String fileName = (String) inputStreamResource.get("fileName");

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(Long.valueOf(inputStreamResource.get("fileSize").toString()))
                .body((InputStreamResource) inputStreamResource.get("file"));

    }

    @GetMapping("search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String name) {
        return new ResponseEntity<>(service.searchStudents(name), HttpStatus.OK);
    }

}
