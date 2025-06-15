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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {

    private StudentService service;

    @Autowired
    public void setService(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public Student saveStudent(@RequestBody Student student) {

        System.out.println(Thread.currentThread().getName() + ", Student Name : " + student.getName());

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
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String name, @RequestParam(required = false) Integer pageNo,
                                                        @RequestParam(required = false) Integer pageSize) {

        if (pageNo == null || pageSize == null) {
            pageNo = 1;
            pageSize = 500;
        }

        return new ResponseEntity<>(service.searchStudents(name, pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/chart-data")
    public Map<String, Object> getChartData(@RequestParam String range) {
        Map<String, Object> response = new HashMap<>();

        switch (range.toUpperCase()) {
            case "1D":
                response.put("labels", List.of("Jun 14"));
                response.put("created", List.of(5));
                response.put("deleted", List.of(2));
                break;

            case "5D":
                response.put("labels", List.of("Jun 10", "Jun 11", "Jun 12", "Jun 13", "Jun 14"));
                response.put("created", List.of(4, 7, 5, 8, 6));
                response.put("deleted", List.of(1, 2, 1, 3, 0));
                break;

            case "1M":
                response.put("labels", List.of("May 20", "May 25", "May 30", "Jun 5", "Jun 10"));
                response.put("created", List.of(3, 6, 4, 9, 7));
                response.put("deleted", List.of(0, 1, 2, 1, 3));
                break;

            case "1Y":
                response.put("labels", List.of("Jul 2023", "Aug 2023", "Sep 2023", "Oct 2023", "Nov 2023", "Dec 2023", "Jan 2024", "Feb 2024", "Mar 2024", "Apr 2024", "May 2024", "Jun 2024"));
                response.put("created", List.of(12, 18, 22, 30, 24, 20, 254545, 28, 35, 40, 38, 500));
                response.put("deleted", List.of(3, 5, 2, 6, 4, 7, 5, 224478, 6, 5, 298, 6));
                break;

            default:
                response.put("labels", List.of());
                response.put("created", List.of());
                response.put("deleted", List.of());
        }

        return response;
    }
}
