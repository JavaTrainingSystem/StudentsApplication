package com.tcs.students.service.impl;

import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dao.StudentRepo;
import com.tcs.students.dto.APIResponse;
import com.tcs.students.dto.Student;
import com.tcs.students.entity.StudentEntity;
import com.tcs.students.service.FileService;
import com.tcs.students.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Primary
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private FileService fileService;

    @Override
    public Student saveStudent(Student student) {

        StudentEntity studentEntity = new StudentEntity();

        BeanUtils.copyProperties(student, studentEntity);

        studentRepo.save(studentEntity);

        student.setId(studentEntity.getId());

        return student;
    }

    @Override
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();

        List<StudentEntity> studentEntities = studentRepo.findAll();

        studentEntities.forEach(studentEntity -> {
            Student student = new Student();
            BeanUtils.copyProperties(studentEntity, student);
            students.add(student);
        });

        return students;
    }

    @Override
    public void deleteStudent(Integer studentId) {
        studentRepo.deleteById(studentId);
    }

    @Override
    public Student updateStudent(Student student) {
        Optional<StudentEntity> studentEntity = studentRepo.findById(student.getId());

        if (studentEntity.isPresent()) {
            BeanUtils.copyProperties(student, studentEntity.get());
            studentRepo.save(studentEntity.get());
        }

        return student;
    }

    @Override
    @Transactional
    public APIResponse uploadStudentDoc(MultipartFile file, Integer studentId) {

        String filePath = fileService.uploadFile(file);

        Optional<StudentEntity> studentEntity = studentRepo.findById(studentId);

        if (studentEntity.isPresent()) {

            if (studentEntity.get().getCertFilePath() != null) {
                fileService.deleteFile(studentEntity.get().getCertFilePath());
            }

            studentEntity.get().setCertFilePath(filePath);
            studentRepo.save(studentEntity.get());
        }

        return new APIResponse(CommonConstants.SUCCESS, 200, "Uploaded Successfully");
    }

    @Override
    public Map<String, Object> downloadStudentDoc(Integer studentId) throws IOException {

        Map<String, Object> response = new HashMap<>();

        Optional<StudentEntity> studentEntity = studentRepo.findById(studentId);

        if (studentEntity.isPresent()) {

            if (studentEntity.get().getCertFilePath() == null) {
                response.put("status", CommonConstants.FAILED);
                response.put("message", "File not uploaded yet. Please upload first.");
                return response;
            }

            InputStream inputStream = fileService.downloadFile(studentEntity.get().getCertFilePath());

            response.put("fileSize", inputStream.available());

            response.put("fileName", new File(studentEntity.get().getCertFilePath()).getName());

            response.put("file", new InputStreamResource(inputStream));

            return response;

        }


        return null;
    }

    @Override
    public List<Student> searchStudents(String name, Integer pageNo, Integer pageSize) {
        List<Student> students = new ArrayList<>();

        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);

        List<StudentEntity> studentEntities = studentRepo.searchStudents(name, pageRequest);

        studentEntities.forEach(studentEntity -> {
            Student student = new Student();
            student.setId(studentEntity.getId());
            student.setName(studentEntity.getName());
            student.setMobile(studentEntity.getMobile());
            students.add(student);
        });


        return students;
    }


}
