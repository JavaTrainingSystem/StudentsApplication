package com.tcs.students.dao;

import com.tcs.students.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepo extends JpaRepository<StudentEntity, Integer> {


    @Query("SELECT s FROM StudentEntity s WHERE s.name LIKE  %:name%")
    List<StudentEntity> searchStudents(@Param(value = "name") String name);


}
